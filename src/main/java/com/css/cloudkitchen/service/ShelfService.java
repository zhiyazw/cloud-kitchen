package com.css.cloudkitchen.service;

import com.css.cloudkitchen.constants.Temperature;
import com.css.cloudkitchen.entity.Food;
import com.css.cloudkitchen.entity.Order;
import com.css.cloudkitchen.entity.shelf.Impl.ColdShelf;
import com.css.cloudkitchen.entity.shelf.Impl.FrozenShelf;
import com.css.cloudkitchen.entity.shelf.Impl.HotShelf;
import com.css.cloudkitchen.entity.shelf.Impl.OverflowShelf;
import com.css.cloudkitchen.entity.shelf.Shelf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Shelf Service
 * @author Joe Ding
 */
@Service
@Slf4j
public class ShelfService {
    @Autowired
    private HotShelf hotShelf;

    @Autowired
    private ColdShelf coldShelf;

    @Autowired
    private FrozenShelf frozenShelf;

    @Autowired
    private OverflowShelf overflowShelf;

    @Autowired
    private CourierService courierService;

    /**
     * Usually, a Food is placed into the Shelf which has the same temperature properties as the Order of the Food.
     *
     * In case that the Shelf talked above is full, the Food is placed into the Overflow Shelf if it has free room.
     *
     * Otherwise, we determine the behavior in two cases.
     *
     * First, find all kinds of temperatures with existing Food in Overflow Shelf, then determine which Shelf
     * among these temperatures has more that zero free room and has the maximun free room. If such one exists,
     * move a Food with allowable temperature from Overlow Shelf to that Shelf, then place the new Food into Overflow Shelf.
     *
     * Second, if no shelf talked above exists, find the food with lowest value in Overflow Shelf, discard it,
     * then place the new Food into Overflow Shelf.
     * @param food The food just received
     * @throws Exception Throw Exception if the order has an unsupported temperature
     */
    public void putInShelf(Food food) throws Exception {
        log.info("Receive food, try to determine shelf " + food);
        Shelf targetShelf = getShelfByTemperature(food.getOrder().getTemp());
        if(!targetShelf.hasRoom()) {
            log.info("Target " + targetShelf + " is full, try to put to Overflow shelf");
            if(!overflowShelf.hasRoom()) {
                log.info("Overflow shelf " + overflowShelf + " is full, try to free a room");
                Shelf backShelf = findBackShelf();
                if(backShelf != null) {
                    Food backFood = overflowShelf.getFoodByTemperature(backShelf.getName());
                    if(backFood != null) {
                        overflowShelf.take(backFood.getOrder().getId());
                        backShelf.put(backFood);
                        log.info("Found backShelf " + backShelf + ", move " + backFood + " from Overflow shelf to backShelf");
                    }
                } else {
                    Food leastValueFood = overflowShelf.getLeastValueFood();
                    if(leastValueFood != null) {
                        courierService.cancelDelivery(leastValueFood.getOrder());
                        overflowShelf.take(leastValueFood.getOrder().getId());
                        log.info("No backShelf available, discard " + leastValueFood + " from Overflow shelf");
                    }
                }
            }
            targetShelf = overflowShelf;
        }
        log.info("Put " + food + " in " + targetShelf);
        targetShelf.put(food);
    }

    /**
     * Take a food out of shelf. First try to find the food from the shelf with the same temperature as the order,
     * if not found, then try to find from Overflow shelf
     * @param order The order that the food to take corresponds to
     * @return Return the food if found, otherwise null
     * @throws Exception Throw Exception if the order has an unsupported temperature
     */
    public Food takeFood(Order order) throws Exception {
        Shelf shelf = getShelfByTemperature(order.getTemp());
        Food ret = shelf.take(order.getId());
        if(ret == null) {
            ret = overflowShelf.take(order.getId());
        }
        log.info(ret + " is taken out from " + shelf);
        return ret;
    }

    private Shelf findBackShelf() throws Exception {
        int maxRoom = 0;
        Shelf bestShelf = null;
        Set<String> existingTemperature = overflowShelf.getExistingTemperature();
        for(String temp : existingTemperature) {
            Shelf shelf = getShelfByTemperature(temp);
            int room = shelf.getFreeRoom();
            if(room > maxRoom) {
                maxRoom = room;
                bestShelf = shelf;
            }
        }
        return bestShelf;
    }

    private Shelf getShelfByTemperature(String temp) throws Exception {
        switch (temp) {
            case Temperature.HOT:
                return hotShelf;
            case Temperature.COLD:
                return coldShelf;
            case Temperature.FROZEN:
                return frozenShelf;
            default:
                throw new Exception("Unexpected temperature " + temp);
        }
    }

    /**
     * Schedule a regular task to do decay check for all fours shelves periodically
     */
    @Scheduled(fixedRateString = "${shelf.decayCheck.rate}")
    private void decayTask() {
        decay(hotShelf);
        decay(coldShelf);
        decay(frozenShelf);
        decay(overflowShelf);
    }

    private void decay(Shelf shelf) {
        List<Food> wasted = shelf.decay();
        for(Food food : wasted) {
            courierService.cancelDelivery(food.getOrder());
            log.info(food + " on shelf " + shelf+ " is wasted");
        }
    }
}

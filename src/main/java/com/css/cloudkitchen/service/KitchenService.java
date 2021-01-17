package com.css.cloudkitchen.service;


import com.css.cloudkitchen.entity.Food;
import com.css.cloudkitchen.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Kitchen Service
 * @author Joe Ding
 */

@Service
@Slf4j
public class KitchenService {
    @Autowired
    private ShelfService shelfService;

    /**
     * Kitchen receives order, instantly cooks, and puts to shelf
     * @param order The order to cook
     * @throws Exception
     */
    @Async
    public void receiveOrder(Order order) throws Exception {
        log.info("Receive " + order);
        Food food = new Food(order);
        log.info("Cooked " + food);
        shelfService.putInShelf(food);
        log.info("Placed to shelf " + food);
    }

}

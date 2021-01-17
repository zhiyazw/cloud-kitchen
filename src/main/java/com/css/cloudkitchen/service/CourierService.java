package com.css.cloudkitchen.service;

import com.css.cloudkitchen.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

/**
 * Courier Service
 * @author Joe Ding
 */
@Service
@Slf4j
public class CourierService {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ShelfService shelfService;

    @Value("${courier.arrive.fast}")
    private int courierArriveFast;

    @Value("${courier.arrive.slow}")
    private int courierArriveSlow;

    private HashMap<String, Courier> couriers = new HashMap<>(32);

    /**
     * When a new order received, dispatch a courier to go to deliver it
     * @param order The order received
     */
    @Async
    public void receiveOrder(Order order) {

        int delay = new Random().nextInt(courierArriveSlow - courierArriveFast) + courierArriveFast;
        log.info("Receive " + order + ", will arrive in " + delay + " seconds");

        Courier courier = new Courier(order, taskScheduler, this, shelfService);
        courier.go(delay);

        synchronized (couriers) {
            couriers.put(order.getId(), courier);
        }
    }

    /**
     * When an order wasted or deteriorated, notify the CourierService to cancel delivery
     * @param order wasted or deteriorated
     */
    public void cancelDelivery(Order order) {
        synchronized (couriers) {
            Courier courier = couriers.get(order.getId());
            if(courier != null) {
                log.info("Cancel delivery " + order);
                courier.cancelDelivery();
                couriers.remove(order.getId());
            }
        }
    }

    /**
     * Remove the order from CourierService's courier list when it has been delivered
     * @param order The order which has been delivered
     */
    public void removeCourier(Order order) {
        synchronized (couriers) {
            couriers.remove(order.getId());
        }
    }

    public int activeCourierCount() {
        synchronized (couriers) {
            return couriers.size();
        }
    }
}

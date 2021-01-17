package com.css.cloudkitchen.service;

import com.css.cloudkitchen.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * The courier
 * @author Joe Ding
 */
@Slf4j
public class Courier implements Runnable {

    TaskScheduler taskScheduler;
    ShelfService shelfService;
    CourierService courierService;

    private Order order;
    private ScheduledFuture deliverTask;

    public Courier(Order order, TaskScheduler taskScheduler, CourierService courierService, ShelfService shelfService) {
        this.taskScheduler = taskScheduler;
        this.courierService = courierService;
        this.shelfService = shelfService;
        this.order = order;
    }

    /**
     * The courier will spend the time long time to arrive at the kitchen and take the food
     * @param time The time in seconds that the courier will spend to arrive at the kitchen
     */
    public void go(int time) {
        deliverTask = taskScheduler.schedule(this, Date.from(Instant.now().plusSeconds(time)));
    }

    /**
     * The courier cancel it's deliver task upon an order cancel delivery
     */
    public void cancelDelivery() {
        if(deliverTask != null) {
            synchronized (deliverTask) {
                deliverTask.cancel(true);
                deliverTask = null;
            }
        }
    }

    /**
     * When time is up, the courier arrives and takes food
     */
    @Override
    public void run() {
        if(deliverTask != null) {
            synchronized (deliverTask) {
                takeFood();
                deliverTask = null;
            }
        }
    }

    private void takeFood() {
        try {
            shelfService.takeFood(order);
            courierService.removeCourier(order);
            log.info(order + " has been delivered");
        } catch (Exception e) {
            log.error("Take food of " + order + " fail: " + e.getMessage());
        }
    }
}

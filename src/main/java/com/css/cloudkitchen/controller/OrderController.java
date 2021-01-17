package com.css.cloudkitchen.controller;

import com.css.cloudkitchen.common.result.Result;
import com.css.cloudkitchen.common.result.ResultBuilder;
import com.css.cloudkitchen.entity.Order;
import com.css.cloudkitchen.service.CourierService;
import com.css.cloudkitchen.service.KitchenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order Controller
 * @author Joe Ding
 */

@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderController {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private CourierService courierService;

    /**
     * RESTful api for submiting order
     * @param order The order to submit
     * @return Result message returned to caller
     */
    @PostMapping("")
    public Result receiveOrder(@RequestBody Order order) {
        log.info("Receive " + order);
        try {
            kitchenService.receiveOrder(order);
            courierService.receiveOrder(order);
            log.info("Receive Ok: " + order);
            return ResultBuilder.success();
        } catch (Exception e) {
            log.error("Receive fail: " + order);
            return ResultBuilder.error(e.getMessage());
        }
    }
}

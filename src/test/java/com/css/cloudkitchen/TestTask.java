package com.css.cloudkitchen;

import com.css.cloudkitchen.common.result.Result;
import com.css.cloudkitchen.controller.OrderController;
import com.css.cloudkitchen.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class TestTask {
    @Autowired
    OrderController orderController;

    @Async
    public CompletableFuture<Result> runOrder(Order order) {
        Result result = orderController.receiveOrder(order);
        return CompletableFuture.completedFuture(result);
    }
}

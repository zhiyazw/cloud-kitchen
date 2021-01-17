package com.css.cloudkitchen;

import com.css.cloudkitchen.controller.OrderController;
import com.css.cloudkitchen.entity.Order;
import com.css.cloudkitchen.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Test application
 * @author Joe Ding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@EnableAsync
@Slf4j
public class CloudKitchenApplicationTests {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    OrderController orderController;

    @Autowired
    CourierService courierService;

    @Autowired
    TestTask testTask;

    @Value("${order.speed}")
    int orderSpeed;

    @Test
    public void contextLoads() {
    }

    @Test
    public void runAllOrders() throws IOException, JSONException {
        ClassPathResource resource = new ClassPathResource("orders.json");
        InputStream inputStream = resource.getInputStream();

        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, Order.class);
        List<Order> orders = mapper.readValue(inputStream, collectionType);

        for(int i = 0; i < orders.size(); i++) {
            testTask.runOrder(orders.get(i));
            try {
                Thread.sleep(orderSpeed);
            } catch (InterruptedException e) {
            }
        }

        while(courierService.activeCourierCount() > 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}

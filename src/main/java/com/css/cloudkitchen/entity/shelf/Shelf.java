package com.css.cloudkitchen.entity.shelf;

import com.css.cloudkitchen.entity.Food;

import java.util.List;

/**
 * Shelf interface
 * @author Joe Ding
 */
public interface Shelf {
    int getCapacity();

    String getName();

    int getFreeRoom();

    boolean hasRoom();

    void put(Food food) throws Exception;

    Food take(String id);

    List<Food> decay();
}

package com.css.cloudkitchen.entity.shelf.Impl;

import com.css.cloudkitchen.entity.Food;
import com.css.cloudkitchen.entity.shelf.Shelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The base implement of Shelf
 * @author Joe Ding
 */
public abstract class BaseShelf implements Shelf {
    protected int capacity;
    protected float decayModifier;
    protected String name;
    protected HashMap<String, Food> rooms;

    public BaseShelf(int capacity, float decayModifier, String name) {
        this.capacity = capacity;
        this.decayModifier = decayModifier;
        this.name = name;
        rooms = new HashMap<>(this.capacity);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Check if current shelf has free room
     * @return Return true if current shelf has free room, otherwise false
     */
    @Override
    public boolean hasRoom() {
        return rooms.size() < capacity;
    }

    /**
     * Get the free room count of current shelf
     * @return Return the free room count of current shelf
     */
    @Override
    public int getFreeRoom() {
        return capacity - rooms.size();
    }

    /**
     * Put the food into current shelf, and set the decayModifer of current shelf to the food
     * @param food The food to put in
     * @throws Exception Throw Exception if current shelf is full
     */
    @Override
    public void put(Food food) throws Exception{
        synchronized (rooms) {
            if (rooms.size() == capacity) {
                throw new Exception(this + " is full");
            }

            food.setShelfDecayModifier(decayModifier);
            rooms.put(food.getOrder().getId(), food);
        }
    }

    /**
     * Take the food with order id out of current shelf
     * @param id The order id of which a food to take
     * @return Return the food just removed, or null no food with this order id exists.
     */
    @Override
    public Food take(String id) {
        synchronized (rooms) {
            return rooms.remove(id);
        }
    }

    /**
     * Remove and return all foods with value equal or less than zero
     * @return Return all foods with value equal or less than zero. An empty list is returned when no such food exists.
     */
    @Override
    public List<Food> decay() {
        List<Food> ret = new ArrayList<>();
        synchronized (rooms) {
            List<String> ids = new ArrayList<>(rooms.keySet());
            for(String id : ids) {
                Food food = rooms.get(id);
                if(food.value() <= 0) {
                    ret.add(food);
                    rooms.remove(id);
                }
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "BaseShelf{" +
            "decayModifier=" + decayModifier +
            ", name='" + name + '\'' +
            ", size=" + rooms.size() +
            ", capacity=" + capacity +
            '}';
    }
}

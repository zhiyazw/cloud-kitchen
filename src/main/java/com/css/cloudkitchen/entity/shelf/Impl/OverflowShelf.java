package com.css.cloudkitchen.entity.shelf.Impl;

import com.css.cloudkitchen.constants.ShelfNames;
import com.css.cloudkitchen.entity.Food;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The overflow shelf
 */
@Component
public class OverflowShelf extends BaseShelf {
    public OverflowShelf(@Value("${shelf.overflow.capacity}") int capacity, @Value("${shelf.overflow.decayModifier}") float decayModifier) {
        super(capacity, decayModifier, ShelfNames.OVERFLOW);

    }

    /**
     * Get all kinds of temperatures of all foods in this overflow shelf
     * @return Return a set of temperatures, or a empty set if the shelf is empty
     */
    public Set<String> getExistingTemperature() {
        synchronized (rooms) {
            return rooms.values().stream().map(o->o.getOrder().getTemp()).collect(Collectors.toSet());
        }
    }

    /**
     * Get food by spefified temperature
     * @param temperature The temperature of which a order to look for
     * @return Return a food with specified temperature, if more than one orders have that temperature, which one is returned is undefined. Return null if on order with specified temperature exists
     */
    public Food getFoodByTemperature(String temperature) {
        synchronized (rooms) {
            Optional<Food> result = rooms.values().stream().filter(o->o.getOrder().getTemp().equals(temperature)).findFirst();
            return result.isPresent() ? result.get() : null;
        }
    }

    /**
     * Get the food with least value in overlow shelf
     * @return Return the food with least value in overflow shelf, or null if overflow shelf is empty
     */
    public Food getLeastValueFood() {
        synchronized (rooms) {
            double minValue = Double.MAX_VALUE;
            Food ret = null;
            for(Food food : rooms.values()) {
                double value = food.value();
                if(value < minValue) {
                    minValue = value;
                    ret = food;
                }
            }
            return ret;
        }
    }

    @Override
    public String toString() {
        return "OverflowShelf{" +
            super.toString() +
            '}';

    }
}

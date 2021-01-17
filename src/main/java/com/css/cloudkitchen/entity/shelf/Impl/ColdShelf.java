package com.css.cloudkitchen.entity.shelf.Impl;

import com.css.cloudkitchen.constants.Temperature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The Cold Shelf
 * @author Joe Ding
 */
@Component
public class ColdShelf extends BaseShelf {
    public ColdShelf(@Value("${shelf.cold.capacity}") int capacity, @Value("${shelf.cold.decayModifier}") float decayModifier)  {
        super(capacity, decayModifier, Temperature.COLD);
    }

    @Override
    public String toString() {
        return "ColdShelf{" +
            super.toString() +
            '}';
    }
}

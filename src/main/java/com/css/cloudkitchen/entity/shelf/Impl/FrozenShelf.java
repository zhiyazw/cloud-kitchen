package com.css.cloudkitchen.entity.shelf.Impl;

import com.css.cloudkitchen.constants.Temperature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The frozen shelf
 * @author Joe Ding
 */
@Component
public class FrozenShelf extends BaseShelf {
    public FrozenShelf(@Value("${shelf.frozen.capacity}") int capacity, @Value("${shelf.frozen.decayModifier}") float decayModifier) {
        super(capacity, decayModifier, Temperature.FROZEN);

    }

    @Override
    public String toString() {
        return "FrozenShelf{" +
            super.toString() +
            '}';
    }
}

package com.css.cloudkitchen.entity.shelf.Impl;

import com.css.cloudkitchen.constants.Temperature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The hot shelf
 * @author Joe Ding
 */
@Component
public class HotShelf extends BaseShelf {
    public HotShelf(@Value("${shelf.hot.capacity}") int capacity, @Value("${shelf.hot.decayModifier}") float decayModifier) {
        super(capacity, decayModifier, Temperature.HOT);
    }

    @Override
    public String toString() {
        return "HotShelf{" +
            super.toString() +
            '}';
    }
}

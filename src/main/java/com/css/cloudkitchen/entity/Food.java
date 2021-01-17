package com.css.cloudkitchen.entity;

import java.util.Date;

/**
 * The Food entity, which wrap an order, plus produceTime and shelfDecayModifier properties
 * @author Joe Ding
 */
public class Food {
    private Order order;
    private Date produceTime;

    private float shelfDecayModifier;

    public Food(Order order) {
        this.order = order;
        produceTime = new Date();
    }

    public Order getOrder() {
        return order;
    }

    public float getShelfDecayModifier() {
        return shelfDecayModifier;
    }

    /**
     * When this food is put into a shelf, set shelfDecayModifier to decayModifier of that shelf
     * @param shelfDecayModifier decayModifier of the shelf which current food is put to
     */
    public void setShelfDecayModifier(float shelfDecayModifier) {
        this.shelfDecayModifier = shelfDecayModifier;
        // TODO: caculate food value for each individual shelfDecayModifier
    }

    /**
     * Calculate Food age (in second), which is now time subtracts produceTime
     * @return Return Food age
     */
    public long age() {
        Date now = new Date();
        return (now.getTime() - produceTime.getTime()) / 1000;
    }

    /**
     * Calculate Food value according formula (shelfLife - orderAge - decayRate * orderAge * shelfDecayModifier) / shelfLife
     * @return Return Food value
     */
    public double value() {
        int shelfLife = order.getShelfLife();
        float decayRate = order.getDecayRate();
        long age = age();
        return (shelfLife - age - decayRate * age * shelfDecayModifier) / shelfLife;
    }

    @Override
    public String toString() {
        return "Food{" +
            "order=" + order +
            ", produceTime=" + produceTime +
            ", shelfDecayModifier=" + shelfDecayModifier +
            ", age=" + age() +
            ", value=" + value() +
            '}';
    }
}

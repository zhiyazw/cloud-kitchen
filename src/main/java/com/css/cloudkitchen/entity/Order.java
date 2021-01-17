package com.css.cloudkitchen.entity;

/**
 * The Order entity
 * @author Joe Ding
 */
public class Order {

    private String id;
    private String name;
    private String temp;
    private int shelfLife;
    private float decayRate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public float getDecayRate() {
        return decayRate;
    }

    public void setDecayRate(float decayRate) {
        this.decayRate = decayRate;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", temp='" + temp + '\'' +
            ", shelfLife=" + shelfLife +
            ", decayRate=" + decayRate +
            '}';
    }
}

package com.driver.model;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        String[] dTime=deliveryTime.split(":");
        int hrs=Integer.parseInt(dTime[0]);
        int minutes=Integer.parseInt(dTime[1]);

        this.deliveryTime=(hrs*60)+minutes;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}

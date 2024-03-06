package com.rng.billgenerator;

public class Item {
    private String itemName;
    private int quantity;
    private double totalWeight;
    private double rate;
    private double subtotal;

    public Item(String itemName, int quantity, double totalWeight, double rate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalWeight = totalWeight;
        this.rate = rate;
        calculateSubtotal(); // Calculate subtotal immediately on object creation
    }

    

    public String getItemName() {
        return itemName;
    }



    public void setItemName(String itemName) {
        this.itemName = itemName;
    }



    public int getQuantity() {
        return quantity;
    }



    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public double getTotalWeight() {
        return totalWeight;
    }



    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }



    public double getRate() {
        return rate;
    }



    public void setRate(double rate) {
        this.rate = rate;
    }



    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }



    public void calculateSubtotal() {
        this.subtotal = totalWeight * rate;
    }

    public double getSubtotal() {
        return subtotal;
    }
}

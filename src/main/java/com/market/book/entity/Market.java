package com.market.book.entity;


public class Market {

    private int size;
    private int price;
    private String status;


    public Market(int price, int size, String status) {
        this.price = price;
        this.size = size;
        this.status = status;

    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
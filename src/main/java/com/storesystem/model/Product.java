package com.storesystem.model;

public class Product {
    private long code;
    private String name;
    private double price;
    private int categoryId;
    private int stock;
    public Product() {
        this.code = 0;
    }
    public Product(String name, long price, int stock, int categoryId) {
        this.code = 0;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }
    public long getCode() {return code;}
    public void setCode(long code) {
        this.code = code;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}
    public int getCategoryId() {return categoryId;}
    public void setCategoryId(int categoryId) {this.categoryId = categoryId;}
    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

}
package com.storesystem.model;

public class OrderItem {
    private long productId;
    private String productName;
    private int productQuantity;
    private long productUnitPrice;
    private long totalPrice;
    public  OrderItem() {

    }
    public OrderItem(long productId, String productName, int productQuantity, long productUnitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productUnitPrice = productUnitPrice;
        this.totalPrice = productQuantity * productUnitPrice;
    }
    public OrderItem(long productId, String productName, int productQuantity, long productUnitPrice, long totalPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productUnitPrice = productUnitPrice;
        this.totalPrice = totalPrice;
    }
    public long getProductId() {return productId;}
    public void setProductId(long productId) {this.productId = productId;}
    public String getProductName() {return productName;}
    public void setProductName(String productName) {
        if(productName == null || productName.trim().isEmpty()){
            throw new IllegalArgumentException("OrderItem : Product name cannot be null or empty");
        }
        this.productName = productName.trim();
    }
    public int getProductQuantity() {return productQuantity;}
    public void setProductQuantity(int productQuantity) {
        if(productQuantity < 0){
            throw new IllegalArgumentException("OrderItem : Product quantity cannot be negative");
        }
        this.productQuantity = productQuantity;
        this.totalPrice = this.productQuantity * this.productUnitPrice;
    }
    public long getProductUnitPrice() {return productUnitPrice;}
    public void setProductUnitPrice(long productUnitPrice) {
        if(productUnitPrice <= 0){
            throw new IllegalArgumentException("OrderItem : Product unit price cannot be less than 0!");
        }
        this.productUnitPrice = productUnitPrice;
        this.totalPrice = this.productUnitPrice * this.productQuantity;
    }
    public long getTotalPrice() {return totalPrice;}
    public void setTotalPrice(long totalPrice) {this.totalPrice = totalPrice;}

}

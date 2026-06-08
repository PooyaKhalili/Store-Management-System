package com.storesystem.model;
import java.util.List;

public class Order {
    private long customerId;
    private long orderId;
    private String orderDate;
    private long totalAmount;
    private List<OrderItem> orderItems;
    public Order() {
        this.orderId = 0;
    }
    public Order(long customerId, String orderDate, List<OrderItem> orderItems) {
        this.orderId = 0;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
        this.totalAmount = calculateTotalAmount();
    }
    public Order(long orderId, long customerId, String orderDate, long totalAmount, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }
    public long calculateTotalAmount(){
        long sum = 0;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                sum += item.getTotalPrice();
            }
        }
        return sum;
    }
    public long getCustomerId() {return customerId;}
    public void setCustomerId(long customerId) {this.customerId = customerId;}
    public long getOrderId() {return orderId;}
    public void setOrderId(long orderId) {this.orderId = orderId;}
    public String getOrderDate() {return orderDate;}
    public void setOrderDate(String orderDate) {this.orderDate = orderDate;}
    public long getTotalAmount() {return totalAmount;}
    public void setTotalAmount(long totalAmount) {this.totalAmount = totalAmount;}
    public List<OrderItem> getOrderItems() {return orderItems;}
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        this.totalAmount = calculateTotalAmount();
    }

}
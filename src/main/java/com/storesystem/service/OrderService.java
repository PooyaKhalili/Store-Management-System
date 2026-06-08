package com.storesystem.service;

import com.storesystem.model.Order;
import com.storesystem.model.OrderItem;
import com.storesystem.repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(long customerId, String orderDate, List<OrderItem> items) {
        validateOrder(customerId, orderDate, items);
        Order order = new Order(customerId, orderDate, items);
        return orderRepository.addOrder(order);
    }

    public Order getOrderById(long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllOrders();
    }


    public void deleteOrder(long orderId) {
        Order order = orderRepository.findOrderById(orderId);
        if (order == null)
            throw new IllegalArgumentException("Order not found");
        orderRepository.deleteOrder(order);
    }

    public List<Order> getOrdersByCustomerId(long customerId) {
        return orderRepository.findOrdersByCustomerId(customerId);
    }

    private void validateOrder(long customerId, String orderDate, List<OrderItem> items) {
        if (customerId <= 0)
            throw new IllegalArgumentException("Customer id is invalid");
        if (orderDate == null || orderDate.trim().isEmpty())
            throw new IllegalArgumentException("Order date cannot be empty");
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Order must contain at least one item");
    }
}

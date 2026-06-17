package com.storesystem.repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.storesystem.model.Order;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import com.storesystem.util.JalaliDateUtil;

public class OrderRepository {
    private final String filePath = "src\\data\\Orders.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<Long, Order> storage = new HashMap<>();
    private long nextOrderId = 1;

    public OrderRepository() {
        loadData();
    }

    public void loadData() {
        try (Reader reader = new FileReader(filePath)) {
            Type typeList = new TypeToken<ArrayList<Order>>() {}.getType();
            List<Order> orders = gson.fromJson(reader, typeList);

            if (orders != null && !orders.isEmpty()) {
                for (Order order : orders) {
                    long id = order.getOrderId();
                    storage.put(id, order);
                    if (id >= nextOrderId) {
                        nextOrderId = id + 1;
                    }
                }
            }
        }catch (FileNotFoundException e) {
            System.out.println("loadData() : File not found");
        }
        catch (IOException e) {
            System.out.println("OrderRepository: Error reading file");
        }
    }

    public void saveData() {
        try (Writer writer = new FileWriter(filePath)) {
            List<Order> orders = new ArrayList<>(storage.values());
            gson.toJson(orders, writer);
        } catch (IOException e) {
            System.out.println("OrderRepository: Error saving file");
        }
    }

    private long generateOrderId() {
        return nextOrderId++;
    }

    public Order addOrder(Order order) {
        if (order.getOrderId() == 0) {
            order.setOrderId(generateOrderId());
        }

        if (storage.containsKey(order.getOrderId())) {
            throw new IllegalArgumentException("Order with id " + order.getOrderId() + " already exists!");
        }

        storage.put(order.getOrderId(), order);
        saveData();
        return order;
    }
    public void deleteAllOrders() {
        storage.clear();
        nextOrderId = 1;
        saveData();
    }
    public Order findOrderById(long orderId) {return storage.get(orderId);}

    public List<Order> findAllOrders() {return new ArrayList<>(storage.values());}
    public void deleteOrder(Order order) {
        if (order.getOrderId() == 0) {
            throw new IllegalArgumentException("Cannot delete order with id 0");
        }

        if (!storage.containsKey(order.getOrderId())) {
            throw new IllegalArgumentException("Order with id " + order.getOrderId() + " does not exist!");
        }

        storage.remove(order.getOrderId());
        saveData();
    }

    public List<Order> findOrdersByCustomerId(long customerId) {
        List<Order> foundOrders = new ArrayList<>();
        for (Order order : storage.values()) {
            if (order.getCustomerId() == customerId) {
                foundOrders.add(order);
            }
        }
        return foundOrders;
    }

    public List<Order> findOrdersByDate(String date) {
        List<Order> foundOrders = new ArrayList<>();
        for(Order order : storage.values()) {
            if(JalaliDateUtil.compareJalaliDates(order.getOrderDate(), date) == 0) {
                foundOrders.add(order);
            }
        }
        return foundOrders;
    }

    public List<Order> findOrdersByDateRange(String startDate, String endDate) {
        if(JalaliDateUtil.compareJalaliDates(startDate, endDate) > 0) {
            throw new IllegalArgumentException("Cannot find orders between " + startDate + " and " + endDate);
        }
        List<Order> foundOrders = new ArrayList<>();
        for(Order order : storage.values()) {
            if((JalaliDateUtil.compareJalaliDates(startDate, order.getOrderDate()) <= 0) && (JalaliDateUtil.compareJalaliDates(order.getOrderDate(), endDate) <= 0)) {
                foundOrders.add(order);
            }
        }
        return foundOrders;
    }
}
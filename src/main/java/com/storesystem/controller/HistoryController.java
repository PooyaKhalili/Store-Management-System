package com.storesystem.controller;

import com.storesystem.model.Order;
import com.storesystem.model.OrderItem;
import com.storesystem.view.HistoryPanel;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;
public class HistoryController  {
    private final HistoryPanel historyPanel;
    private final DecimalFormat priceFormatter = new DecimalFormat("###,###,###");
    public HistoryController(HistoryPanel historyPanel) {
        this.historyPanel = historyPanel;
    }
    public List<Object[]> getAllOrders(){
        List<Order> orders = OrderController.orderService.getAllOrders();
        List<Object[]> list = new ArrayList<>();
        for(Order order : orders){
            Object[]row  = {
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getTotalAmount(),
                    priceFormatter.format(order.getTotalAmount()),
                    "0",
                    "0",
                    priceFormatter.format(order.getTotalAmount())
            };
            list.add(row);
        }
        return list;
    }
    public String deleteOrder(int orderId){
        try {
            OrderController.orderService.deleteOrder(orderId);
            return "سفارش با موفقیت حذف شد";
        }catch(Exception e){
            return "خطا: " + e.getMessage();
        }

    }
    public String deleteAllOrders() {
        try {
            OrderController.orderService.deleteAllOrders();
            return "تمامی سفارش ها با موفقیت حذف شدند";
        } catch (Exception e) {
            return "خطا: " + e.getMessage();
        }
    }
    public List<Object[]> getOrderItems(long orderId) {
        try {
            Order order = OrderController.orderService.getOrderById(orderId);
            List<Object[]> list = new ArrayList<>();
            if (order != null && order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    Object[] row = {
                            item.getProductName(),
                            priceFormatter.format(item.getProductUnitPrice()),
                            item.getProductQuantity(),
                            priceFormatter.format(item.getTotalPrice())
                    };
                    list.add(row);
                }
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    public List<Object[]> searchByDateRange(String startDate, String endDate) {
        List<Order> orders;
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            orders = OrderController.orderService.getOrdersByDateRange(startDate, endDate);
        } else if (startDate != null && !startDate.isEmpty()) {
            orders = OrderController.orderService.getOrdersByDate(startDate);
        } else {
            orders = OrderController.orderService.getAllOrders();
        }
        return convertToTableData(orders);
    }
    public List<Object[]> searchOrders(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllOrders();
        }

        List<Order> results = new ArrayList<>();

        try {
            long orderId = Long.parseLong(searchText.trim());
            Order order = OrderController.orderService.getOrderById(orderId);
            if (order != null) {
                results.add(order);
            }
        } catch (NumberFormatException e) {
            //search by OrderId
        }

        try {
            long customerId = Long.parseLong(searchText.trim());
            List<Order> customerOrders = OrderController.orderService.getOrdersByCustomerId(customerId);
            results.addAll(customerOrders);
        } catch (NumberFormatException e) {
            //search by CustomerId
        }

        List<Order> unique = new ArrayList<>();
        for (Order order : results) {
            if (!unique.contains(order)) {
                unique.add(order);
            }
        }

        return convertToTableData(unique);
    }
    public List<Object[]> convertToTableData(List<Order> orders) {
        List<Object[]> list = new ArrayList<>();
        for (Order order : orders) {
            Object[] row = {
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getOrderDate(),
                    order.getTotalAmount(),
                    priceFormatter.format(order.getTotalAmount()),
                    "0",
                    "0",
                    priceFormatter.format(order.getTotalAmount())
            };
            list.add(row);
        }
        return list;
    }
    public void refreshTopTable() {
        historyPanel.refreshTopTable(getAllOrders());
    }

    public void clearBottomTable() {
        historyPanel.clearBottomTable();
    }
    public void showOrderItems(long orderId) {
        List<Object[]> items = getOrderItems(orderId);
        historyPanel.refreshBottomTable(items);
    }
}
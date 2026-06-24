package com.storesystem.service;


import com.storesystem.model.Customer;
import com.storesystem.model.Order;
import com.storesystem.model.OrderItem;
import com.storesystem.model.Product;
import com.storesystem.repository.CustomerRepository;
import com.storesystem.repository.OrderRepository;
import com.storesystem.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;

    public ReportService(OrderService orderService,ProductService productService,CustomerService customerService){
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
    }

    public Double getTotalAmount(){
        List<Order> orders = orderService.getAllOrders();
        Double total = 0D;
        for(var order : orders)
            total+=order.getTotalAmount();
        return total;
    }

    public List<Object[]> getTopsellingProducts(){
        List<Object[]> result = new ArrayList<>(10);
        List<Order> orders = orderService.getAllOrders();
        for(var order : orders){
            List<OrderItem> items = order.getOrderItems();
            for(var item : items){
                long code = item.getProductId();
                int quantity = item.getProductQuantity();
                Product product = productService.getProductByCode(code);
                if(product==null) continue;
                String name = product.getName();
                boolean found = false;
                for(var row : result){
                    String existingName = (String) row[0];
                    if(existingName.equals(name)){
                        Integer currentQuantity = (Integer) row[1];
                        row[1] = currentQuantity + quantity;
                        found = true;
                        break;
                    }
                }
                if(!found){
                    result.add(new Object[]{name,quantity});
                }
            }
        }
        result.sort((row1, row2) -> ((Integer) row2[1]).compareTo((Integer) row1[1]));
        return result;
    }

    public List<Object[]> getCustomerPurchaseSummary(){
        List<Object[]> result = new ArrayList<>(10);
        List<Order> orders = orderService.getAllOrders();
        for(var order : orders){
            long customerId = order.getCustomerId();
            if (customerId <= 0) continue;
            Customer customer = customerService.getCustomerById((int)customerId);
            if(customer==null) continue;
            String customerName = customer.getFirstName() + " " + customer.getLastName();
            double orderAmount = order.getTotalAmount();
            boolean found = false;
            for(var row : result){
                String existingName = (String)row[0];
                if(existingName.equals(customerName)){
                    int orderCount = (Integer) row[1];
                    double totalPurchase = (Double)row[2];
                    row[1] = orderCount + 1;
                    row[2] = totalPurchase + orderAmount;
                    found = true;
                    break;
                }
            }
            if(!found){
                result.add(new Object[]{customerName,1,orderAmount});
            }
        }
        result.sort((row1, row2) -> ((Double) row2[2]).compareTo((Double) row1[2]));
        return result;
    }
}

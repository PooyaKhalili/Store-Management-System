package com.storesystem.util;

import com.storesystem.config.InvoicePdfService;
import com.storesystem.repository.CustomerRepository;
import com.storesystem.repository.OrderRepository;
import com.storesystem.repository.ProductRepository;
import com.storesystem.service.*;

public class AppContext {

    public static final ProductRepository productRepository = new ProductRepository();
    public static final CustomerRepository customerRepository = new CustomerRepository();
    public static final OrderRepository orderRepository = new OrderRepository();

    public static final ProductService productService = new ProductService(productRepository);
    public static final CustomerService customerService = new CustomerService(customerRepository);
    public static final OrderService orderService = new OrderService(orderRepository, customerRepository);
    public static final ReportService reportService = new ReportService(orderService, productService, customerService);
    public static final InvoicePdfService invoicePdfService = new InvoicePdfService(customerService);

    private AppContext() {
    }
}


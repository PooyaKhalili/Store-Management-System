package com.storesystem.controller;

import com.storesystem.config.InvoiceTxtService;
import com.storesystem.model.*;
import com.storesystem.config.InvoicePdfService;
import com.storesystem.util.AppContext;
import com.storesystem.service.OrderService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.OrderPanel;

import javax.swing.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static com.storesystem.util.JalaliDateUtil.getCurrentJalaliDateTime;


public class OrderController {

    private OrderService orderService;
    private final OrderPanel orderPanel;
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

    public OrderController(OrderPanel orderPanel){
        this.orderPanel = orderPanel;
        this.orderService = AppContext.orderService;
    }

    public class CartItem {
        public Product product;
        public int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public long getTotalPrice() {
            return (long) (product.getPrice() * quantity);
        }
    }

    private List<CartItem> currentCart = new ArrayList<>();

    public void loadCustomersIntoComboBox() {
        try {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("مشتری عمومی"); 

            List<Customer> customers = CustomerController.customerService.getAllCustomers();
            if (customers != null) {
                for (Customer customer : customers) {
                    model.addElement(customer.getId() + " - " + customer.getFirstName() + " " + customer.getLastName() + " - " + customer.getPhoneNumber());
                }
            }
            orderPanel.comboBox1.setModel(model);
        } catch (Exception e) {
            System.err.println("خطا در لود مشتریان: " + e.getMessage());
        }
    }

    public void searchCustomer(String text) {
        try {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("مشتری عمومی");

            List<Customer> customers = new ArrayList<>();
            if (text != null && !text.trim().isEmpty()) {
                if (CustomerController.customerService.searchByFirstName(text) != null) customers.addAll(CustomerController.customerService.searchByFirstName(text));
                if (CustomerController.customerService.searchByLastName(text) != null) customers.addAll(CustomerController.customerService.searchByLastName(text));
                if (CustomerController.customerService.searchByPhone(text) != null) customers.addAll(CustomerController.customerService.searchByPhone(text));
                
                List<Customer> unique = new ArrayList<>();
                for (Customer c : customers) {
                    boolean duplicate = false;
                    for (Customer u : unique) {
                        if (u.getId() == c.getId()) { duplicate = true; break; }
                    }
                    if (!duplicate) {
                        unique.add(c);
                        model.addElement(c.getId() + " - " + c.getFirstName() + " " + c.getLastName()+ " - " + c.getPhoneNumber());
                    }
                }
            } else {
                this.loadCustomersIntoComboBox(); 
                return;
            }
            orderPanel.comboBox1.setModel(model);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadCategoriesIntoComboBox() {
        try {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("همه");
            List<Category> categories = CategoryController.categoryService.getAllCategories();
            if (categories != null) {
                for (Category category : categories) {
                    model.addElement(category.getName());
                }
            }
            orderPanel.comboBox2.setModel(model);
        } catch (Exception e) {
            System.err.println("خطا در لود دسته‌بندی‌ها: " + e.getMessage());
        }
    }

    public void searchProductForOrder(String text, String categoryName) {
        List<Product> uniqueProducts = new ArrayList<>();

        if (text != null && !text.trim().isEmpty()) {
            List<Product> allResults = new ArrayList<>();
            if (ProductController.productService.searchProductsByName(text) != null) allResults.addAll(ProductController.productService.searchProductsByName(text));
            try {
                long code = Long.parseLong(text.trim());
                Product productByCode = ProductController.productService.getProductByCode(code);
                if (productByCode != null) allResults.add(productByCode);
            } catch (Exception e) {}

            for (Product currentProduct : allResults) {
                boolean isDuplicate = false;
                for (Product savedProduct : uniqueProducts) {
                    if (savedProduct.getCode() == currentProduct.getCode()) { isDuplicate = true; break; }
                }
                if (!isDuplicate) uniqueProducts.add(currentProduct);
            }
        } else {
            if (ProductController.productService.getAllProducts() != null) {
                uniqueProducts.addAll(ProductController.productService.getAllProducts());
            }
        }

        if (categoryName != null && !categoryName.equals("همه")) {
            int targetCategoryId = getCategoryIdByName(categoryName);
            List<Product> filteredByCategory = new ArrayList<>();
            for (Product p : uniqueProducts) {
                if (p.getCategoryId() == targetCategoryId) filteredByCategory.add(p);
            }
            uniqueProducts = filteredByCategory;
        }

        List<Object[]> data = new ArrayList<>();
        for (Product product : uniqueProducts) {
            Object[] row = {
                product.getCode(), 
                product.getName(), 
                priceFormatter.format(product.getPrice()), 
                product.getStock(), 
                getCategoryNameById(product.getCategoryId())
            };
            data.add(row);
        }
        TableUtil.refreshTable(orderPanel.cartTable, data);
    }

    public String addToCart(long productCode, int quantity) {
        if (quantity <= 0) return "تعداد باید بیشتر از صفر باشد!";
        
        try {
            Product product = ProductController.productService.getProductByCode(productCode);
            if (product == null) return "کالا یافت نشد!";

            CartItem existingItem = null;
            for (CartItem item : currentCart) {
                if (item.product.getCode() == productCode) {
                    existingItem = item;
                    break;
                }
            }
            int totalRequestedQuantity = quantity + (existingItem != null ? existingItem.quantity : 0);
            if (!ProductController.productService.hasSufficientStock(productCode, totalRequestedQuantity)) {
                return "موجودی انبار کافی نیست! (موجودی فعلی: " + product.getStock() + ")";
            }
            if (existingItem != null) {
                existingItem.quantity = totalRequestedQuantity;
            } else {
                currentCart.add(new CartItem(product, quantity));
            }
            refreshCartTable();
            return "کالا به سبد خرید اضافه شد";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage() != null ? e.getMessage() : "خطای سیستمی!";
        }
    }

    public String editCartItem(long productCode, int newQuantity) {
        if (newQuantity <= 0) return "تعداد باید بیشتر از صفر باشد!";

        try {
            if (!ProductController.productService.hasSufficientStock(productCode, newQuantity)) {
                return "موجودی انبار کافی نیست!";
            }

            for (CartItem item : currentCart) {
                if (item.product.getCode() == productCode) {
                    item.quantity = newQuantity;
                    refreshCartTable();
                    return "تعداد با موفقیت ویرایش شد";
                }
            }
            return "کالا در سبد خرید یافت نشد!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public void deleteFromCart(long productCode) {
        CartItem itemToRemove = null;
        for (CartItem item : currentCart) {
            if (item.product.getCode() == productCode) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            currentCart.remove(itemToRemove);
            refreshCartTable();
        }
    }

    public void refreshCartTable() {
        List<Object[]> data = new ArrayList<>();
        long totalSum = 0;

        for (CartItem item : currentCart) {
            long itemTotal = item.getTotalPrice();
            totalSum += itemTotal;

            Object[] row = {
                item.product.getCode(),
                item.product.getName(),
                priceFormatter.format(item.product.getPrice()),
                item.quantity,
                priceFormatter.format(itemTotal)
            };
            data.add(row);
        }

        TableUtil.refreshTable(orderPanel.productTable, data);
        orderPanel.totalSumLabel.setText("جمع کل: " + priceFormatter.format(totalSum) + " ریال");
    }


    public String checkout(String customerDetails) {
        if (currentCart.isEmpty()) {
            return "سبد خرید خالی است!";
        }

        try {
            if (customerDetails == null || customerDetails.trim().isEmpty()) {
                return "لطفاً مشتری را انتخاب کنید!";
            }
            long customerId;

            if (customerDetails.equals("مشتری عمومی")) {
                customerId = 1;
            } else {
                String idPart = customerDetails.split("-")[0].trim();
                customerId = Long.parseLong(idPart);
            }

            for (CartItem item : currentCart) {
                if (!ProductController.productService.hasSufficientStock(
                        item.product.getCode(),
                        item.quantity
                )) {
                    return "موجودی کالا «" + item.product.getName() + "» کافی نیست!";
                }
            }

            List<OrderItem> orderItems = new ArrayList<>();

            for (CartItem item : currentCart) {
                orderItems.add(new OrderItem(
                        item.product.getCode(),
                        item.product.getName(),
                        item.quantity,
                        (long) item.product.getPrice()
                ));
            }

            String orderDate = getCurrentJalaliDateTime();

            for (CartItem item : currentCart) {
                ProductController.productService.reduceStock(
                        item.product.getCode(),
                        item.quantity
                );
            }

            Order newOrder = orderService.createOrder(customerId, orderDate, orderItems);

            if (newOrder == null) {
                return "سفارش ثبت نشد!";
            }

            InvoicePdfService invoicePdfService = new InvoicePdfService(CustomerController.customerService);
            File pdfFile;

            InvoiceTxtService txtService = new InvoiceTxtService(CustomerController.customerService);
            File txtFile = txtService.generateInvoice(newOrder);


            try {
                pdfFile = invoicePdfService.generateInvoice(newOrder);
            } catch (Exception pdfException) {
                pdfException.printStackTrace();
                return "سفارش ثبت شد، اما خطا در ساخت PDF: "
                        + (pdfException.getMessage() != null ? pdfException.getMessage() : "خطای نامشخص");
            }

            if (pdfFile == null || !pdfFile.exists() || pdfFile.length() == 0) {
                return "سفارش ثبت شد، اما فایل PDF ساخته نشد یا فایل خالی است.";
            }

            try {
                invoicePdfService.openPdf(pdfFile);
            } catch (Exception openException) {
                openException.printStackTrace();

                currentCart.clear();
                refreshCartTable();

                return "فاکتور ساخته شد، اما باز نشد.\nمسیر فایل: "
                        + pdfFile.getAbsolutePath();
            }

            currentCart.clear();
            refreshCartTable();

            return "فاکتور با موفقیت ثبت شد و PDF ساخته شد.\nمسیر فایل: "
                    + pdfFile.getAbsolutePath();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "فرمت اطلاعات مشتری نامعتبر است!";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage() != null ? e.getMessage() : "خطای سیستمی در ثبت فاکتور!";
        }
    }



    private int getCategoryIdByName(String name) {
        try {
            for (Category category : CategoryController.categoryService.getAllCategories()) {
                if (category.getName().equals(name)) return category.getId();
            }
        } catch (Exception e) {}
        return -1; 
    }

    private String getCategoryNameById(int id) {
        try {
            Category category = CategoryController.categoryService.getCategoryById(id);
            if (category != null) return category.getName();
        } catch (Exception e) {}
        return "نامشخص";
    }
}
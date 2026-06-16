package com.storesystem.controller;

import com.storesystem.model.Product;
import com.storesystem.model.Customer;
import com.storesystem.model.Category;
import com.storesystem.model.OrderItem;
import com.storesystem.repository.CustomerRepository;
import com.storesystem.repository.OrderRepository;
import com.storesystem.service.OrderService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.OrderPanel;

import javax.swing.DefaultComboBoxModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static com.storesystem.util.JalaliDateUtil.getCurrentJalaliDateTime;


public class OrderController {

    static final OrderService orderService = new OrderService(new OrderRepository(), new CustomerRepository());
    private final OrderPanel orderPanel;
    private final DecimalFormat priceFormatter = new DecimalFormat("#,###");

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

    public OrderController(OrderPanel orderPanel) {
        this.orderPanel = orderPanel;
    }

    // ==================================================
    // ۱. لود و جستجوی مشتریان (comboBox1)
    // ==================================================
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
            long customerId = 0;
            if (customerDetails != null && !customerDetails.equals("مشتری گذری (عمومی)")) {
                try {
                    String[] parts = customerDetails.split(" - ");
                    customerId = Long.parseLong(parts[0].trim());
                } catch (Exception e) {
                    return "فرمت مشتری انتخاب شده نامعتبر است.";
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
                ProductController.productService.reduceStock(item.product.getCode(), item.quantity);
            }

            orderService.createOrder(customerId, orderDate, orderItems);

            currentCart.clear();
            refreshCartTable();
            searchProductForOrder(null, "همه"); 

            return "فاکتور با موفقیت ثبت و سفارش ذخیره شد.";

        } catch (Exception e) {
            e.printStackTrace();
            return "خطا در تسویه: " + e.getMessage();
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
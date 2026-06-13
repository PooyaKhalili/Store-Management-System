package com.storesystem.controller;

import com.storesystem.model.Product;
import com.storesystem.model.Category;
import com.storesystem.repository.ProductRepository;
import com.storesystem.repository.CategoryRepository;
import com.storesystem.service.ProductService;
import com.storesystem.service.CategoryService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.ProductPanel;
import javax.swing.DefaultComboBoxModel;
import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductController {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final CategoryService categoryService = new CategoryService(new CategoryRepository());
    private final ProductPanel productPanel;
    private final DecimalFormat priceFormatter = new DecimalFormat("###,###,###");


    public ProductController(ProductPanel productPanel) {
        this.productPanel = productPanel;
    }

    public String addProduct(String name, String priceStr, String stockStr, String categoryName){
        try {
            long price = Long.parseLong(priceStr.replace(",", ""));
            int stock = Integer.parseInt(stockStr);
            int categoryId = getCategoryIdByName(categoryName);

            for(Product product : productService.getAllProducts()){
                if (name.equals(product.getName())) {
                    return "کالا با این نام قبلا ثبت شده است";
                }
            }

        productService.createProduct(name, price, stock, categoryId);
        } catch(NumberFormatException e){
            return "مقادیر قیمت و موجودی باید به صورت عدد معتبر باشند";
        } catch(Exception e){
            return e.getMessage();
        }
        
        TableUtil.refreshTable(productPanel.productTable, this.getAllProducts());
        return "کالا با موفقیت اضافه شد";
    }

    public List<Object[]> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<Object[]> data = new ArrayList<>();
        for(Product product : products){
            String catName = getCategoryNameById(product.getCategoryId());
            String formattedPrice = priceFormatter.format(product.getPrice());
            Object[] row = {
                product.getCode(), 
                product.getName(), 
                formattedPrice,
                product.getStock(), 
                catName
            };
            data.add(row);
        }
        return data;
    }

    public String editProduct(long code, String name, String priceStr, String stockStr, String categoryName){
        Product selectedProduct;
        try{
            selectedProduct = productService.getProductByCode(code);
        }catch(Exception e){
            return e.getMessage();
        }

        try{
            long price = Long.parseLong(priceStr.replace(",", ""));
            int stock = Integer.parseInt(stockStr);
            int categoryId = getCategoryIdByName(categoryName);

            for(Product product : productService.getAllProducts()){
                if (name.equals(product.getName()) && product.getCode() != selectedProduct.getCode()) {
                    return "کالا با این نام قبلا ثبت شده است";
                }
            }

        productService.updateProduct(code, name, price, stock, categoryId);
        }catch(NumberFormatException e){
            return "مقادیر قیمت و موجودی باید به صورت عدد معتبر باشند";
        }catch(Exception e){
            return e.getMessage();
        }

        TableUtil.refreshTable(productPanel.productTable, this.getAllProducts());
        return "کالا با موفقیت ویرایش شد";
    }

    public void deleteProduct(long code){
        try{
            productService.deleteProduct(code);
        }catch(Exception e){
            return; 
        }
        TableUtil.refreshTable(productPanel.productTable, this.getAllProducts());
    }

    public void loadCategoriesIntoComboBox() {
        try {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            model.addElement("همه");

            List<Category> categories = categoryService.getAllCategories();
            
            if (categories != null) {
                for (Category category : categories) {
                    model.addElement(category.getName());
                }
            }

            productPanel.comboBox.setModel(model);

        } catch (Exception e) {
            System.err.println("خطا در لود کردن دسته‌بندی‌ها: " + e.getMessage());
        }
    }

    public void searchProduct(String text, String categoryName) {
        List<Product> uniqueProducts = new ArrayList<>();

        if (text != null && !text.trim().isEmpty()) {
            
            List<Product> allResults = new ArrayList<>();

            if (productService.searchProductsByName(text) != null) {
                allResults.addAll(productService.searchProductsByName(text));
            }

            try {
                long code = Long.parseLong(text.trim());
                Product productByCode = productService.getProductByCode(code);
                if (productByCode != null) {
                    allResults.add(productByCode);
                }
            } catch (Exception e) {

            }

            for (Product currentProduct : allResults) {
                boolean isDuplicate = false;
                
                for (Product savedProduct : uniqueProducts) {
                    if (savedProduct.getCode() == currentProduct.getCode()) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    uniqueProducts.add(currentProduct);
                }
            }

        } else {
            uniqueProducts.addAll(productService.getAllProducts());
        }

        if (categoryName != null && !categoryName.equals("همه")) {
            int targetCategoryId = getCategoryIdByName(categoryName);
            List<Product> filteredByCategory = new ArrayList<>();
            
            for (Product p : uniqueProducts) {
                if (p.getCategoryId() == targetCategoryId) {
                    filteredByCategory.add(p);
                }
            }
            uniqueProducts = filteredByCategory;
        }

        List<Object[]> data = new ArrayList<>();
        for (Product product : uniqueProducts) {
            String formattedPrice = priceFormatter.format(product.getPrice());
            Object[] row = {
                product.getCode(), 
                product.getName(), 
                formattedPrice, 
                product.getStock(), 
                getCategoryNameById(product.getCategoryId())
            };
            data.add(row);
        }

        TableUtil.refreshTable(productPanel.productTable, data);
    }

    public void showLowStockProducts() {
        try {
            List<Product> lowStockProducts = productService.getLowStockProducts();
            List<Object[]> data = new ArrayList<>();
            for (Product product : lowStockProducts) {
                Object[] row = {
                    product.getCode(), 
                    product.getName(), 
                    product.getPrice(), 
                    product.getStock(), 
                    getCategoryNameById(product.getCategoryId())
                };
                data.add(row);
            }
            TableUtil.refreshTable(productPanel.productTable, data);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    private int getCategoryIdByName(String name) {
        for (Category category : categoryService.getAllCategories()) {
            if (category.getName().equals(name)) {
                return category.getId();
            }
        }
        return -1; 
    }

    private String getCategoryNameById(int id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return category.getName();
        } catch (Exception e) {
            return "نامشخص";
        }
    }
}
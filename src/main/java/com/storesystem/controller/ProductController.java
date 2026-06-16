package com.storesystem.controller;

import com.storesystem.model.Product;
import com.storesystem.model.Category;
import com.storesystem.repository.ProductRepository;
import com.storesystem.service.ProductService;
import com.storesystem.util.CsvUtil;
import com.storesystem.util.TableUtil;
import com.storesystem.view.ProductPanel;
import javax.swing.DefaultComboBoxModel;
import java.io.FileNotFoundException;
import java.util.List;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductController {

    static final ProductService productService = new ProductService(new ProductRepository());
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
                if (name.equals(product.getName()) && !(price == product.getPrice())) {
                    return "کالا با این نام قبلا ثبت شده است";
                }
                else if(name.equals(product.getName()) && (price == product.getPrice())) {
                    productService.updateProduct(product.getCode(), product.getName(), product.getPrice(), product.getStock()+stock, product.getCategoryId());
                    TableUtil.refreshTable(productPanel.productTable, this.getAllProducts());
                    return "کالای مورد نظر از قبل موجود بوده و  موجودی جدید به لیست اضافه شد";
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

            List<Category> categories = CategoryController.categoryService.getAllCategories();
            
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
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    private int getCategoryIdByName(String name) {
        for (Category category : CategoryController.categoryService.getAllCategories()) {
            if (category.getName().equals(name)) {
                return category.getId();
            }
        }
        return -1; 
    }

    private String getCategoryNameById(int id) {
        try {
            Category category = CategoryController.categoryService.getCategoryById(id);
            return category.getName();
        } catch (Exception e) {
            return "نامشخص";
        }
    }
    public String exportToCsv(String filePath) {
        try {
            List<Product> allProducts = productService.getAllProducts();
            if (allProducts.isEmpty()) {return "هیچ محصولی برای خروجی گرفتن وجود ندارد!";}
            List<String[]> data = new ArrayList<>();
            for (Product product : allProducts) {
                String[] row = {
                        String.valueOf(product.getCode()),
                        product.getName(),
                        String.valueOf(product.getPrice()),
                        String.valueOf(product.getStock()),
                        getCategoryNameById(product.getCategoryId())
                };
                data.add(row);
            }
            CsvUtil.writeCsvFile(filePath, data);
            return "عملیات خروجی گرفتن از فایل CSV با موفقیت انجام شد!";
        }catch (Exception e){
            return "خطا در هنگام اجرای عملیات";
        }
    }
    public String importFromCsv(String filePath) {
        try {
            List<String[]> importData = CsvUtil.readCsvFile(filePath);
            if (importData.isEmpty()) {
                return "فایل CSV خالی است!";
            }

            for (String[] row : importData) {
                if (row.length >= 5) {
                    String name = row[1];
                    String price = row[2];
                    String stock = row[3];
                    String category = row[4];

                    String result = addProduct(name, price, stock, category);
                }
            }
            TableUtil.refreshTable(productPanel.productTable, getAllProducts());
            loadCategoriesIntoComboBox();

        } catch (FileNotFoundException e) {
            return "فایل موردنظر برای گرفتن ورودی یافت نشد";
        }catch (Exception e){
            return "خطا در هنگام خواندن فایل";
        }
        return "عملیات ورودی گرفتن از فایل CSV با موفقیت انجام شد";
    }
}
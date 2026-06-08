package com.storesystem.service;

import com.storesystem.model.Product;
import com.storesystem.repository.ProductRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, double price, int stock, int categoryId) {
        validateProduct(name, price, stock, categoryId);
        Product product = new Product(name, (long) price, stock, categoryId);
        return productRepository.addProduct(product);
    }

    public void updateProduct(long code, String name, double price, int stock, int categoryId) {
        Product product = productRepository.searchProductByCode(code);
        if (product == null)
            throw new IllegalArgumentException("Product not found");
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
        productRepository.editProduct(product);
    }

    public void deleteProduct(long code) {
        Product product = productRepository.searchProductByCode(code);
        if (product == null)
            throw new IllegalArgumentException("Product not found");
        productRepository.deleteProduct(product);
    }

    public Product getProductByCode(long code) {
        Product product = productRepository.searchProductByCode(code);
        if (product == null)
            throw new IllegalArgumentException("Product not found");
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productRepository.searchProductsByCategory(categoryId);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.searchProductByName(name);
    }

    public boolean hasSufficientStock(long code, int quantity) {
        return productRepository.hasSufficientStock(code, quantity);
    }

    public void reduceStock(long code, int quantity) {
        productRepository.reduceStock(code, quantity);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.lowStockProducts();
    }

    private void validateProduct(String name, double price, int stock, int categoryId) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Product name cannot be empty");
        if (price <= 0)
            throw new IllegalArgumentException("Price must be positive");
        if (stock < 0)
            throw new IllegalArgumentException("Stock cannot be negative");
        if (categoryId <= 0)
            throw new IllegalArgumentException("Category id is invalid");
    }
}

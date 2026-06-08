package com.storesystem.service;

import com.storesystem.model.Category;
import com.storesystem.repository.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        validateCategoryName(name);
        if (categoryRepository.findCategoryByName(name) != null)
            throw new IllegalArgumentException("Category already exists");
        Category category = new Category(name);
        return categoryRepository.saveCategory(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllCategories();
    }

    public Category getCategoryById(int id) {
        if (!categoryRepository.isIdFound(id))
            throw new IllegalArgumentException("Category with id " + id + " not found");
        return categoryRepository.findCategoryById(id);
    }

    public Category updateCategory(int id, String newName) {
        validateCategoryName(newName);
        if (!categoryRepository.isIdFound(id))
            throw new IllegalArgumentException("Category with id " + id + " not found");
        Category category = categoryRepository.findCategoryById(id);
        category.setName(newName);
        return categoryRepository.saveCategory(category);
    }

    public void deleteCategory(int id) {
        if (!categoryRepository.isIdFound(id))
            throw new IllegalArgumentException("Category with id " + id + " not found");
        categoryRepository.deleteCategoryById(id);
    }

    private void validateCategoryName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Category name cannot be empty");
    }
}

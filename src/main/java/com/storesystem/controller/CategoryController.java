package com.storesystem.controller;

import com.storesystem.model.Category;
import com.storesystem.repository.CategoryRepository;
import com.storesystem.service.CategoryService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.CategoryPanel;
import java.util.List;
import java.util.ArrayList;

public class CategoryController {

    private final CategoryService categoryService = new CategoryService(new CategoryRepository());
    private final CategoryPanel categoryPanel;

    public CategoryController(CategoryPanel categoryPanel) {
        this.categoryPanel = categoryPanel;
    }

    public String addCategory(String name) {
        // بررسی تکراری بودن دقیقاً به سبک خودت
        for(Category category : categoryService.getAllCategories()){
            // در جاوا برای مقایسه متن‌ها حتماً از equals استفاده می‌کنیم تا باگ ایجاد نشود
            if (name.equals(category.getName())) {
                return "دسته‌بندی با این نام قبلا ثبت شده است";
            }
        }
        try{
            categoryService.createCategory(name);
        }catch(Exception e){
            return e.getMessage();
        }
        // دسترسی مستقیم به متغیر public پنل (table)
        TableUtil.refreshTable(categoryPanel.table, this.getAllCategories());
        return "دسته‌بندی با موفقیت اضافه شد";
    }

    public List<Object[]> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        List<Object[]> data = new ArrayList<>();
        for(Category category : categories){
            Object[] row = {category.getId(), category.getName()};
            data.add(row);
        }
        return data;
    }

    public String editCategory(int id, String name){
        Category selectedCategory = categoryService.getCategoryById(id);
        try{
           for(Category category : categoryService.getAllCategories()){
            if (name.equals(category.getName()) && category.getId() != selectedCategory.getId()) {
                return "دسته‌بندی با این نام قبلا ثبت شده است";
            }
           }
        }catch(Exception e){
            return e.getMessage();
        }
        
        try{
            categoryService.updateCategory(id, name);
        }catch(Exception e){
            return e.getMessage();
        }
        TableUtil.refreshTable(categoryPanel.table, this.getAllCategories());
        return "دسته‌بندی با موفقیت ویرایش شد";
    }

    public void deleteCategory(int id){
        try{
            categoryService.deleteCategory(id);
        }catch(Exception e){
            return;
        }
        TableUtil.refreshTable(categoryPanel.table, this.getAllCategories());
    }
}
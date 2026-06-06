package com.storesystem.repository;
import com.storesystem.model.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryRepository {
    private final String filePath = "data/Categories.json";
    private int id = 1;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<Integer, Category> storage = new HashMap<>();
    public CategoryRepository() {
        loadData();
    }
    public void loadData() {
        try(Reader reader = new FileReader(filePath)){
            Type listType = new TypeToken<ArrayList<Category>>(){}.getType();
            List<Category> categories = new Gson().fromJson(reader, listType);
            if(!categories.isEmpty()){
                for(Category c : categories){
                    int cId  = c.getId();
                    storage.put(cId, c);
                    if(cId >= id){
                        id = cId+1;
                    }
                }
            }
        }
        catch(IOException e){
            System.out.println("CategoryRepository : File not found");
        }
    }
    public void saveData() {
        try(Writer writer = new FileWriter(filePath)){
            List<Category> categories = new ArrayList<>(storage.values());
            gson.toJson(categories, writer);
        }
        catch(IOException e){
            System.out.println("CategoryRepository : File writing failed");
        }
    }
    public Category findCategoryById(int id){
        return storage.get(id);
    }
    public Category findCategoryByName(String categoryName){
        for(Category c : storage.values()){
            if(c.getName().equalsIgnoreCase(categoryName)){
                return c;
            }
        }
        return null;
    }
    public Category saveCategory(Category category){
        if(category.getId() == 0){
            category.setId(id);
            storage.put(category.getId(), category);
            id++;
        }else{
            if(!storage.containsKey(category.getId())){
                throw new IllegalArgumentException("Category with id " + category.getId() + " does not exist");
            }
            storage.put(category.getId(), category);
        }
        saveData();
        return category;
    }
    public void deleteCategoryById(int id){
        if(!storage.containsKey(id)){
            throw new IllegalArgumentException("Category with id " + id + " does not exist, so it cannot be deleted");
        }
        storage.remove(id);
        saveData();
    }
    public boolean isIdFound(int id){
        return storage.containsKey(id);
    }
    public List<Category> findAllCategories(){
        return new ArrayList<>(storage.values());
    }


}

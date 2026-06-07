package com.storesystem.model;

public class Category {
    String name;
    int id;
    public Category() {
        this.name = "";
        this.id = 0;
    }
    public Category(String name) {
        this.name = name;
        this.id = 0;
    }
    public Category(int id, String name) {
        this.name = name;
        this.id = id;
    }
    public String getName() {return name;}
    public void setName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("CategoryModel : Name cannot be empty");
        }
        this.name = name;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}

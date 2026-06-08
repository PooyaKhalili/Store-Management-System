package com.storesystem.repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.storesystem.model.Product;
import java.util.Random;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
public class ProductRepository {
    private final String filePath = "data/Products.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<Long, Product> storage = new HashMap<>();
    private Set<Long>usedCodes = new HashSet<>();
    private Random random = new Random();
    public ProductRepository() {
        try {
            loadData();
        }catch (FileNotFoundException e){
            System.out.println("loadData() method :  File not found");
        }
    }
    public void loadData() throws FileNotFoundException {
        File file = new File(filePath);
        try(Reader reader = new FileReader(filePath)){
            Type typeList = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> products = gson.fromJson(reader,typeList);
            if(products!=null &&  !products.isEmpty()){
                for(Product product : products){
                    long code = product.getCode();
                    storage.put(code, product);
                    usedCodes.add(code);
                }
            }
        } catch (IOException e) {
            System.out.println("ProductRepository : File not found");
        }

    }
    public void saveData(){
        try(Writer writer = new FileWriter(filePath)){
            List<Product> products = new ArrayList<>(storage.values());
            gson.toJson(products, writer);
        }
        catch (IOException e){
            System.out.println("ProductRepository : File writing failed");
        }
    }
    public Product addProduct(Product product){
        if(product.getCode()==0){
            long newCode = generateCode();
            product.setCode(newCode);
        }
        if(storage.containsKey(product.getCode())){
            throw new IllegalArgumentException("Product already exists!");
        }
        storage.put(product.getCode(), product);
        usedCodes.add(product.getCode());
        saveData();
        return product;
    }
    public void editProduct(Product product){
        if(product.getCode()==0){
            throw new IllegalArgumentException("ProductRepository: Product does not exist!");
        }
        if(!storage.containsKey(product.getCode())){
            throw new IllegalArgumentException("ProductRepository: Product with code"+product.getCode()+" does not exist!");
        }
        storage.replace(product.getCode(), product);
        saveData();
    }
    public void deleteProduct(Product product){
        if(product.getCode()==0){
            throw new IllegalArgumentException("ProductRepository: Product does not exist!");
        }
        if(!storage.containsKey(product.getCode())){
            throw new IllegalArgumentException("ProductRepository: Product with code "+product.getCode()+" does not exist!");
        }
        storage.remove(product.getCode());
        saveData();
    }
    public List<Product> searchProductsByCategory(int categoryId){
        List<Product> foundproducts = new ArrayList<>();
        for(Product product : storage.values()){
            if(categoryId == product.getCategoryId()){
                foundproducts.add(product);
            }
        }
        return foundproducts;

    }
    public List<Product> searchProductByName(String productName){
        List<Product> foundproducts = new ArrayList<>();
        for(Product product : storage.values()){
            if(product.getName().toLowerCase().contains(productName.toLowerCase())){
                foundproducts.add(product);
            }
        }
        return foundproducts;
    }
    public List<Product> findAllProducts(){
        return new ArrayList<>(storage.values());
    }
    public Product searchProductByCode(long code){
        return storage.get(code);
    }
    public long generateCode() {
        long code;
        do {
            code = 100000 + random.nextInt(900000);
        }while(usedCodes.contains(code));
        return code;
    }
    public boolean hasSufficientStock(long code, int quantity) {
        Product product = searchProductByCode(code);
        if(product == null){throw new IllegalArgumentException("ProductRepository: Product with code "+code+" does not exist!");}
        if(product.getStock() < quantity){return false;}
        return true;
    }
    public void reduceStock(long code, int quantity){
        Product product = searchProductByCode(code);
        if(product == null){throw new IllegalArgumentException("ProductRepository: Product with code "+code+" does not exist!");}
        if(product.getStock() >= quantity){
            product.setStock(product.getStock() - quantity);
            saveData();
        }
        else{throw new IllegalArgumentException("InSufficient Stock");}
    }
    public List<Product> lowStockProducts(){
        List<Product> foundProducts = new ArrayList<>();
        for(Product product : storage.values()){
            if(product.getStock()<5){
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }
}
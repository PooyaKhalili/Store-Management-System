package com.storesystem.repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.storesystem.model.Customer;



import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository {
    private final String filePath = "data/Customers.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<Integer, Customer> storage = new HashMap<>();
    private int nextId = 1;
    public CustomerRepository() {
        try {
            loadData();
        }catch (FileNotFoundException e){
            System.out.println("loadData() method :  File not found");
        }
    }
    public void loadData() throws FileNotFoundException {
        File file = new File(filePath);
        try(Reader reader = new FileReader(filePath)){
            Type typeList = new TypeToken<ArrayList<Customer>>(){}.getType();
            List<Customer> customers = gson.fromJson(reader,typeList);
            if(customers != null && !customers.isEmpty()){
                for(Customer customer : customers){
                    int id = customer.getId();
                    storage.put(id, customer);
                    if(id >= nextId){
                        nextId = id + 1;
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("CustomerRepository : File not found");
        }
    }
    public void updateCustomerPurchaseStats(int customerId, long orderAmount) {
        Customer customer = storage.get(customerId);
        if (customer != null) {
            customer.setBuyCount(customer.getBuyCount() + 1);
            customer.setTotalPaid(customer.getTotalPaid() + orderAmount);
            saveData();
        }
    }
    public void saveData(){
        File file = new File(filePath);
        try(Writer writer = new FileWriter(filePath)){
            List<Customer> customers = new ArrayList<>(storage.values());
            gson.toJson(customers, writer);
        }
        catch (IOException e){
            System.out.println("CustomerRepository : File writing failed");
        }
    }
    public int idGenerator(){return nextId++;}
    public Customer addCustomer(Customer customer){
        if(customer.getId()==0){
            customer.setId(idGenerator());
        }
        else{
            if(storage.containsKey(customer.getId())){
                throw new IllegalArgumentException("Customer already exists");
            }
        }
        storage.put(customer.getId(), customer);
        saveData();
        return customer;
    }
    public void editCustomer(Customer customer){
        if(customer.getId() == 0){
            throw new IllegalArgumentException("Can not edit a new customer!");
        }
        if(!storage.containsKey(customer.getId())){
            throw new IllegalArgumentException("Customer does not exist!");
        }
        storage.put(customer.getId(), customer);
        saveData();
    }
    public void deleteCustomer(Customer customer){
        if(customer.getId() == 0){
            throw new IllegalArgumentException("Can not delete a customer with id 0!");
        }
        if(!storage.containsKey(customer.getId())){
            throw new IllegalArgumentException("Customer does not exist!");
        }
        storage.remove(customer.getId());
        saveData();
    }
    public Customer findCustomerById(int id) {return storage.get(id);}
    public List<Customer> findAllCustomers() {return new ArrayList<>(storage.values());}
    public List<Customer>searchCustomersByPhoneNumber(String phoneNumber){
        if(phoneNumber == null){return findAllCustomers();}
        List<Customer> foundCustomers = new ArrayList<>();
        for(Customer customer : storage.values()){
            if(customer.getPhoneNumber().startsWith(phoneNumber)){
                foundCustomers.add(customer);
            }
        }
        return foundCustomers;
    }
    public List<Customer> searchCustomersByFirstName(String firstName){
        if(firstName == null || firstName.isEmpty()){return findAllCustomers();}
        List<Customer> result = new ArrayList<>();
        for (Customer customer : storage.values()) {
            if (customer.getFirstName().toLowerCase().contains(firstName.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }
    public List<Customer> searchCustomersByLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            return findAllCustomers();
        }
        List<Customer> result = new ArrayList<>();
        for (Customer customer : storage.values()) {
            if (customer.getLastName().toLowerCase().contains(lastName.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }

}

package com.storesystem.controller;

import com.storesystem.model.Customer;
import com.storesystem.repository.CustomerRepository;
import com.storesystem.service.CustomerService;
import com.storesystem.util.TableUtil;
import com.storesystem.view.CustomerPanel;
import java.util.List;
import java.util.ArrayList;

public class CustomerController {

    private final CustomerService customerService= new CustomerService(new CustomerRepository());
    private final CustomerPanel customerPanel;

    public CustomerController(CustomerPanel customerPanel) {
        this.customerPanel = customerPanel;
    }

    public String addCustomer(String firstName, String lastName, String phoneNumber){
        for(Customer customer:customerService.searchByPhone(phoneNumber)){
            if (phoneNumber.equals(customer.getPhoneNumber())) {
                return "مشتری با این شماره تلفن قبلا ثبت شده است";
            }
        }
        try{
            customerService.createCustomer(firstName,lastName,phoneNumber);
        }catch(Exception e){
            return e.getMessage();
        }
        TableUtil.refreshTable(customerPanel.CustomerTable, this.getAllCustomers());
        return "مشتری با موفقیت اضافه شد";
    }

    public List<Object[]> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        List<Object[]> data = new ArrayList<>();
        for(Customer customer : customers){
            Object[] row = {customer.getId(), customer.getFirstName() + " " + customer.getLastName(), customer.getPhoneNumber(),customer.getBuyCount(),customer.getTotalPaid(),customer.getRegisteredDate()};
            data.add(row);
        }
        return data;
    }

public void searchCustomer(String text) {
        List<Customer> uniqueCustomers = new ArrayList<>();

        if (text != null && !text.trim().isEmpty()) {
            
            List<Customer> allResults = new ArrayList<>();

            if (customerService.searchByFirstName(text) != null) {
                allResults.addAll(customerService.searchByFirstName(text));
            }
            if (customerService.searchByLastName(text) != null) {
                allResults.addAll(customerService.searchByLastName(text));
            }
            if (customerService.searchByPhone(text) != null) {
                allResults.addAll(customerService.searchByPhone(text));
            }

            try {
                int id = Integer.parseInt(text.trim());
                Customer customerById = customerService.getCustomerById(id);
                if (customerById != null) {
                    allResults.add(customerById);
                }
            } catch (Exception e) {
            }

            for (Customer currentCustomer : allResults) {
                boolean isDuplicate = false;
                
                for (Customer savedCustomer : uniqueCustomers) {
                    if (savedCustomer.getId() == currentCustomer.getId()) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    uniqueCustomers.add(currentCustomer);
                }
            }

        } else {
          uniqueCustomers.addAll(customerService.getAllCustomers());
        }
        List<Object[]> data = new ArrayList<>();
        for (Customer customer : uniqueCustomers) {
            Object[] row = {
                customer.getId(), 
                customer.getFirstName() + " " + customer.getLastName(), 
                customer.getPhoneNumber()
            };
            data.add(row);
        }

        TableUtil.refreshTable(customerPanel.CustomerTable, data);
    }

    public String editCustomer(int id,String firstName, String lastName, String phoneNumber){
        Customer selectedCustomer = customerService.getCustomerById(id);
        try{
           for(Customer customer:customerService.searchByPhone(phoneNumber)){
            if (phoneNumber==customer.getPhoneNumber()&&customer.getId()!=selectedCustomer.getId()) {
                return "مشتری با این شماره تلفن قبلا ثبت شده است";
            }
        }
        }catch(Exception e){
            return e.getMessage();
        }
        try{
            customerService.updateCustomer(id,firstName,lastName,phoneNumber);
        }catch(Exception e){
            return e.getMessage();
        }
        TableUtil.refreshTable(customerPanel.CustomerTable, this.getAllCustomers());
        return "مشتری با موفقیت ویرایش شد";
    }

    public void deleteCustomer(int id){
        try{
            customerService.deleteCustomer(id);
        }catch(Exception e){
            return;
        }
        TableUtil.refreshTable(customerPanel.CustomerTable, this.getAllCustomers());
    }
}

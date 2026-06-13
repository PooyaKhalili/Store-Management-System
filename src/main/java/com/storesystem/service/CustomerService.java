package com.storesystem.service;

import com.storesystem.model.Customer;
import com.storesystem.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(String firstName, String lastName, String phoneNumber) {
        validateCustomer(firstName, lastName, phoneNumber);
        Customer customer = new Customer(firstName, lastName, phoneNumber);
        return customerRepository.addCustomer(customer);
    }

    public void updateCustomer(int id, String firstName, String lastName, String phoneNumber) {
        Customer existingCustomer = customerRepository.findCustomerById(id);
        if (existingCustomer == null)
            throw new IllegalArgumentException("Customer not found");
        validateCustomer(firstName, lastName, phoneNumber);
        existingCustomer.setFirstName(firstName);
        existingCustomer.setLastName(lastName);
        existingCustomer.setPhoneNumber(phoneNumber);
        customerRepository.editCustomer(existingCustomer);
    }

    public void deleteCustomer(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null)
            throw new IllegalArgumentException("Customer not found");
        customerRepository.deleteCustomer(customer);
    }

    public Customer getCustomerById(int id) {
        Customer customer = customerRepository.findCustomerById(id);
        if (customer == null)
            throw new IllegalArgumentException("Customer not found");
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    public List<Customer> searchByPhone(String phoneNumber) {
        return customerRepository.searchCustomersByPhoneNumber(phoneNumber);
    }

    public List<Customer> searchByFirstName(String firstName) {
        return customerRepository.searchCustomersByFirstName(firstName);
    }

    public List<Customer> searchByLastName(String lastName) {
        return customerRepository.searchCustomersByLastName(lastName);
    }

    private void validateCustomer(String firstName, String lastName, String phoneNumber) {
        if (firstName == null || firstName.trim().isEmpty())
            throw new IllegalArgumentException("نام نمی تواند خالی باشد");
        if (lastName == null || lastName.trim().isEmpty())
            throw new IllegalArgumentException("نام خانوادگی نمی تواند خالی باشد");
        if (!Customer.isPhoneNumberValid(phoneNumber))
            throw new IllegalArgumentException("شماره تلفن نامعتبر است");
    }
}

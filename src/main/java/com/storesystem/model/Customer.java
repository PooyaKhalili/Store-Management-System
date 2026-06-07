package com.storesystem.model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    public Customer() {
        this.id = 0;
    }
    public Customer(String firstName, String lastName, String phoneNumber) {
        this.id = 0;
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
    }
    public Customer(int id, String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {
        if(firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null");
        }
        this.firstName = firstName.trim();
    }
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {
        if(lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null");
        }
        this.lastName = lastName.trim();
    }
    public String getPhoneNumber() {return phoneNumber;}
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public void setPhoneNumber(String phoneNumber) {
        if(isPhoneNumberValid(phoneNumber)){
            this.phoneNumber = phoneNumber;
        }
        else{
            throw new IllegalArgumentException("phone number is not valid");
        }
    }
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.trim().isEmpty()){return false;}
        return phoneNumber.matches("^09[0-9]{9}$");
    }

}

package com.storesystem.model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int buyCount;
    private long totalPaid;
    private String registeredDate;
    public Customer() {

        this.id = 0;
        this.buyCount = 0;
        this.totalPaid = 0;
        //this.registeredDate =
    }
    public Customer(String firstName, String lastName, String phoneNumber, int buyCount, long totalPaid) {
        this.id = 0;
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        this.buyCount = 0;
        this.totalPaid = 0;
        //this.registeredDate =
    }
    public Customer(int id, String firstName, String lastName, String phoneNumber,  int buyCount, long totalPaid) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.buyCount = buyCount;
        this.totalPaid = totalPaid;
        this.registeredDate = registeredDate;
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
    public int getBuyCount() { return buyCount; }
    public void setBuyCount(int buyCount) {
        if (buyCount < 0) throw new IllegalArgumentException("Buy count cannot be negative");
        this.buyCount = buyCount;
    }
    public long getTotalPaid() { return totalPaid; }
    public void setTotalPaid(long totalPaid) {
        if (totalPaid < 0) throw new IllegalArgumentException("Total paid cannot be negative");
        this.totalPaid = totalPaid;
    }
    public void addPurchase(long orderAmount) {
        this.buyCount++;
        this.totalPaid += orderAmount;
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

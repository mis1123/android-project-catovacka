package com.example.catovacka;

public class constructorOfContacts {

    public constructorOfContacts(){

    }

    @Override
    public String toString() {
        return "constructorOfContacts{" +
                "username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public constructorOfContacts(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    private String username;
    private String phoneNumber;



}

package com.example.employeemanagementapp;

public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String jobTitle;
    private String residence;

    public Employee(long id, String firstName, String lastName, String phoneNumber, String email, String jobTitle, String residence) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.jobTitle = jobTitle;
        this.residence = residence;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getResidence() {
        return residence;
    }
}

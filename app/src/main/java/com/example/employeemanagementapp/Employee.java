package com.example.employeemanagementapp;

public class Employee {
//    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String jobTitle;
    private String residence;
    private int imageResource;

    public Employee(String firstName, String lastName, String jobTitle, int imageResource) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.residence = residence;
        this.imageResource = imageResource;
    }



//    public long getId() {
//        return id;
//    }

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
    public int getImageResource(){return imageResource; }
}

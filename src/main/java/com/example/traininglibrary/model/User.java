package com.example.traininglibrary.model;

import jakarta.persistence.Entity;

@Entity
public class User extends Person {

    private String email;
    private String address;
    private String phone;

}

package com.example.traininglibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_user")
public class User extends Person {

    private String email;
    private String address;
    private String phone;

}

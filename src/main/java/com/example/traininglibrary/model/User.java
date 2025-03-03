package com.example.traininglibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "book_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Person {

    private String email;

    private String address;

    private String phone;

}

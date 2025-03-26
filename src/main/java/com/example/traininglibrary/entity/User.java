package com.example.traininglibrary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "book_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends Person {

    @Column(nullable = false)
    private String email;

    private String phone;

    private String address;
}

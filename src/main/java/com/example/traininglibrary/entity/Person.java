package com.example.traininglibrary.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // TPT (Table Per Type)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Persons (authors and users) main class")
public abstract class Person extends BaseIncrementalEntity {

    @Schema(description = "Persons name")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Date of birth of person")
    @JsonProperty("birthDate")
    private LocalDate birthDate;

}

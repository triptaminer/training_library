package com.example.traininglibrary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Základná entita so spoločnými atribútmi")
public class BaseIncrementalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "Jedinečné ID", example = "1")
    @JsonProperty("id")
    private Long id;

    @Version
    @Schema(description = "Verzia entity pre optimistické zamykanie", example = "1")
    @JsonProperty("version")
    private int version;
}

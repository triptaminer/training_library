package com.example.traininglibrary.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Base entity with id and version")
public class BaseIncrementalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "Unique ID", example = "1")
    @JsonProperty("id")
    private Long id;

    @Version
    @Schema(description = "Version of data", example = "1")
    @JsonProperty("version")
    private Long version;
}

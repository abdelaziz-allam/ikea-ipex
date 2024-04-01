package com.ikea.product.catalog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@RecordBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ProductDTO(UUID id,
                         @NotEmpty(message = "Name is mandatory") String name,
                         @NotEmpty(message = "Category is mandatory") String category,
                         @NotEmpty(message = "Description is mandatory") String description,
                         @NotNull(message = "Price is mandatory") double price,
                         @NotEmpty(message = "Image is mandatory") String imageUrl) {
}

package com.example.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO {
    @NotEmpty(message = "Category name cannot be empty")
    private String name;
}

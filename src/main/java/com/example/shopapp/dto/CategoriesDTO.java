package com.example.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO {
    @NotEmpty(message = "Category name cannot be empty")
    private String name;
}

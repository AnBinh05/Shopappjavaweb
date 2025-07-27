package com.example.shopapp.service;

import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory( CategoriesDTO categoriesDTO );
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoriesDTO categoriesDTO);
    void deleteCategory(long id);

}

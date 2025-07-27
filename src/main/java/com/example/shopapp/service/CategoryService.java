package com.example.shopapp.service;

import com.example.shopapp.Repository.CategoryRepository;
import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService  implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoriesDTO categoriesDTO) {
        Category newCategory = Category
                .builder()
                .name(categoriesDTO.getName())
                .build();
        return categoryRepository.save(newCategory);


    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category  updateCategory(long categoryId,CategoriesDTO categoriesDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoriesDTO .getName());
        categoryRepository.save(existingCategory);


        return existingCategory;
    }


    @Override
    public void deleteCategory( long id) {
        //xóa xong
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Category not found"));
    }
}

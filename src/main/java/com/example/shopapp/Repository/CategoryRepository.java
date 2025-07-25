package com.example.shopapp.Repository;

import com.example.shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository   extends JpaRepository<Category, Long> {

}

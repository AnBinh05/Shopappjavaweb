package com.example.shopapp.Controller;

import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Validated
@RequestMapping("${api.prefix}categories")
@RequiredArgsConstructor
public class CategoriesController {
    private  final CategoryService categoryService;
    @PostMapping()
    public ResponseEntity<?> createCategories ( @Valid @RequestBody CategoriesDTO categoriesDTO,
                                                BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()               // Lấy ra tất cả các FieldError
                    .stream()                    // Chuyển thành stream
                    .map(fieldError -> fieldError.getDefaultMessage()) // Lấy ra thông báo lỗi
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoriesDTO);


        System.out.println("DEBUG >> Received name = " + categoriesDTO.getName());

        return ResponseEntity.ok("Add category" + categoriesDTO);
    }
    @GetMapping("")
    public ResponseEntity<List<Category>>getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit

    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(  @PathVariable Long id,
                                                   @Valid @RequestBody CategoriesDTO categoriesDTO
    ){
        categoryService.updateCategory(id, categoriesDTO);
        return ResponseEntity.ok("Update category" +id );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category" + " "+id );

    }

}

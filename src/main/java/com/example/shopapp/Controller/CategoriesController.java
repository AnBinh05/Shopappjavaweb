package com.example.shopapp.Controller;

import com.example.shopapp.dto.CategoriesDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Validated
@RequestMapping("${api.prefix}categories")
public class CategoriesController {
    @GetMapping("")
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit

    ) {
        return ResponseEntity.ok("Page: " + page + ", Limit: " + limit);
    }
    @PostMapping()
    public ResponseEntity<?> addCategories ( @Valid @RequestBody CategoriesDTO categoriesDTO,
                                                  BindingResult result) {
        if(result.hasErrors()) {
          List<String> errorMessages = result.getFieldErrors()               // Lấy ra tất cả các FieldError
                    .stream()                    // Chuyển thành stream
                    .map(fieldError -> fieldError.getDefaultMessage()) // Lấy ra thông báo lỗi
                    .toList();
          return ResponseEntity.badRequest().body(errorMessages);
        }


        System.out.println("DEBUG >> Received name = " + categoriesDTO.getName());

        return ResponseEntity.ok("Add category" + categoriesDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id){
        return ResponseEntity.ok("Update category" +id );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok("Delete category" + " "+id );

    }

}

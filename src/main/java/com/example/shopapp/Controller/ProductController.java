package com.example.shopapp.Controller;

import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.dto.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit

    ) {
        return ResponseEntity.ok("Get products here");
    }
    @GetMapping("/{id}")
    public String getProductById (@PathVariable("id") String productId){
        return "Get product by id" + productId;
    }
    @PostMapping()
    public ResponseEntity<?> createProduct (@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }



            return ResponseEntity.ok("Add category " + productDTO);
        } catch (Exception e) {
            e.printStackTrace(); // Log chi tiết lỗi ra console
            return ResponseEntity
                    .status(500)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProductById (@PathVariable long id){
        return "Delete product succesfully";
    }
}

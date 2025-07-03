package com.example.shopapp.Controller;

import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.dto.ProductDTO;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return "Get product by id" + " "+ productId;
    }
    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct (@Valid  @ModelAttribute ProductDTO productDTO,
                                          // @RequestPart("file") MultipartFile file,
                                            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files =    productDTO.getFiles();
            files =files == null ? new ArrayList<>() : files;
            for (MultipartFile file : files) {
                if (file.getSize() == 0){
                    continue;
                }

                    // Kiểm tra kích thước file và định dạng
                    if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                                .body(" File is too large (>10MB)");


                    }
                    // sau khi kiểm tra kích thước xong thì kiểm tra định dạng
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.badRequest().body(" File is not an image");
                    }
                    // Lưu file và cập nhật thumbnail trong DTO
                    String filename = storeFile(file); // Thay thế hàm này với code của bạn để lưu file
                    // lưu từng đối tượng product  vào trong DB
                // lưu vào bảng product_image






            }

//            {
//                "name": "Sample Product",
//                    "price": 50,
//                    "thumbnail": "https://example.com/image.jpg",
//                    "description": "This is a sample product description.",
//                    "category_id": "12345"
//            }







            return ResponseEntity.ok("Add category successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log chi tiết lỗi ra console
            return ResponseEntity
                    .status(500)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }
    private String storeFile (MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước tên file  để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
    // đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = (java.nio.file.Path) Paths.get("uploads");
        // kiểm tra và tạo thư mục nếu nó không tồn tại
        if(!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;

    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully",id));
    }
}

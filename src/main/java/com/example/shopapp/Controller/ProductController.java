package com.example.shopapp.Controller;

import com.example.shopapp.dto.CategoriesDTO;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.service.IProductService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
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
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("${api.prefix}products")
@AllArgsConstructor
public class ProductController {
    private final IProductService productService;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit

    ) {
        // Tạo Pageable từ thông tin trang và giới hạn
           PageRequest pageRequest = PageRequest.of(page, limit
                   , Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        // Lấy tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build());







    }
    @GetMapping("/{id}")
    public String getProductById (@PathVariable("id") String productId){

        return "Get product by id" + " "+ productId;
    }
    @PostMapping("")
    public ResponseEntity<?> createProduct (@Valid  @RequestBody ProductDTO productDTO,
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
             Product newProduct = productService.createProduct(productDTO);

//            {
//                "name": "Sample Product",
//                    "price": 50,
//                    "thumbnail": "https://example.com/image.jpg",
//                    "description": "This is a sample product description.",
//                    "category_id": "12345"
//            }







            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            e.printStackTrace(); // Log chi tiết lỗi ra console
            return ResponseEntity
                    .status(500)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage ( @ModelAttribute("files") List<MultipartFile> files
                                          , @PathVariable("id") Long productId){
        try {
            Product existingProduct =  productService.getProductById(productId);
            files =files == null ? new ArrayList<>() : files;
            if(files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body(" you cannot upload more than 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
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
                ProductImage productImage= productService.createProductImage(existingProduct
                        .getId(), ProductImageDTO.builder()

                        .imageUrl(filename)
                        .build());
                productImages.add(productImage);

                // lưu vào bảng product_image






            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }



    }
    private String storeFile (MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }

            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById (@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully",id));
    }
}

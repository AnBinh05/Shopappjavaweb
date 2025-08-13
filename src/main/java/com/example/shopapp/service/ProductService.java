package com.example.shopapp.service;

import com.example.shopapp.Repository.CategoryRepository;
import com.example.shopapp.Repository.ProductImageRepository;
import com.example.shopapp.Repository.ProductRepository;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class ProductService  implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        //System.out.println("DEBUG >>> categoryId = " + productDTO.getCategoryId());
         Category existingCategory= categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()->
                        new DataNotFoundException("cannot find category with id :"+productDTO.getCategoryId()));
   Product newProduct = Product.builder()
           .name(productDTO.getName())
           .price(productDTO.getPrice())
           .thumbnail(productDTO.getThumbnail())
           .description(productDTO.getDescription())
           .category(existingCategory)


           .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws Exception{
        return productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException(
                        "cannot find product with id :"+productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        // Lấy danh sách sản phẩm theo trang (page) và giới hạn (limit)

//Biến đổi (transform) từng Product lấy từ DB thành một ProductResponse.

//Đây là cách chuyển từ Entity sang DTO.
        return productRepository.findAll(pageRequest)

                .map(product -> ProductResponse.fromProduct(product));




    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct != null) {
            //copy các thuộc tính từ DTO -> Product
            //Có thể sử dụng ModelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find category with id: "+productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }


        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }




    @Override
    public boolean existsByName(String name) {

    return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(Long productId,ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find product with id: "+productImageDTO.getProductId()))  ;
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Ko cho insert quá 5 ảnh cho 1 sản phẩm
        int size  = productImageRepository.findByProductId(productId).size();
        if(size >= 5){
            throw new InvalidParamException(" number of image should be less than 5 ! ");
        }
        return productImageRepository.save(newProductImage);


    }


}

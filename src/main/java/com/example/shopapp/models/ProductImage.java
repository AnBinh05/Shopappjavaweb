package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn( name = "product_id")
    private Product product;

    @Column( name = "image_url", length = 300)
    private String imageUrl;
}

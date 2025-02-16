package com.diamond.store.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private int stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Size> sizes;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Image> images;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<Review> reviews;

    @ManyToMany(mappedBy = "products")
    private List<Sale> onSales;
}

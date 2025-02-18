package com.diamond.store.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String saleId;
    private String saleName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String saleType;//Loại giảm giá (Tiền hay %)
    private double value;
    private Date timeStart;
    private Date timeEnd;
    private boolean enable; //Enable and disable sale holiday

    @ManyToMany
    @JoinTable(
            name = "sale_products",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


    public void addProduct(Product product) {
        this.products.add(product);
        product.getOnSales().add(this);
    }
}

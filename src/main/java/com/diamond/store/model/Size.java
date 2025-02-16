package com.diamond.store.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sizeId;
    String sizeName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;


}

package com.diamond.store.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String productName;

    private String description;
    private double price;
    private int stock;

    private String category;

    private String collection;

    private String brand;

    private List<String> sizes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MultipartFile> images;

}

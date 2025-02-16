package com.diamond.store.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeRequest {

    private String attributeName;
    private String description;
}

package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForm {
    private String clientSkuId;
    private String name;
    private String brand;
    private String mrp;
    private String description;
}

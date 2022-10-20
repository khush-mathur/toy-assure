package com.increff.model.data;

import com.increff.model.form.ProductForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductData extends ProductForm {
    private String clientName;
    private Long globalSkuId;
}

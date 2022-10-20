package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCsvForm {
    private String clientSkuId;
    private Long orderQuantity;
    private String sellingPrice;
}

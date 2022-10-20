package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannOrderCsvForm {
    private String channSkuId;
    private Long orderQuantity;
    private String sellingPrice;
}

package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinSkuForm {
    private Long binId;
    private String clientSkuId;
    private Long quantity;
}

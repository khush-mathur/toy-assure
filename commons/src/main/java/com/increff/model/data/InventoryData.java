package com.increff.model.data;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InventoryData {
    private Long id;
    private String clientSkuId;
    private String clientName;
    private String productName;
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}

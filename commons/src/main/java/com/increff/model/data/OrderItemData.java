package com.increff.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class OrderItemData {
    private Long id;
    private Long orderId;
    private String client;
    private String clSku;
    private Long orderedQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
    private Double sellingPricePerUnit;
}

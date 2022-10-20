package com.increff.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrderItemData {
    private Long id;
    private Long orderId;
    private String channel;
    private String client;
    private String channSku;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
}

package com.increff.model.data;

import com.increff.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderData {
    private Long id;
    private String clientName;
    private String customerName;
    private String channelName;
    private String channelOrderId;
    private OrderStatus status;
}

package com.increff.model.form;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannOrderForm {
    private String channelName;
    private String chanOrderId;
    private String clientName;
    private String customerName;
    private List<ChannOrderCsvForm> orderItems;
}

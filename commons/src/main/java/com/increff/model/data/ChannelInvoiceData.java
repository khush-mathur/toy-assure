package com.increff.model.data;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelInvoiceData {
    private Long orderId;
    private Double total;
    private List<ChannelOrderItemData> orderItems;
    private String invoiceTime;
    private String clientName;
    private String channelName;
}

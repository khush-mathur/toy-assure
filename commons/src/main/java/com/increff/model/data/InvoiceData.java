package com.increff.model.data;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
@Setter
@XmlRootElement
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceData {
    private Long orderId;
    private Double total;
    private List<OrderItemData> orderItems;
    private String invoiceTime;
    private String clientName;
}
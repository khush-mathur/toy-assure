package com.increff.pojo;

import com.increff.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;

import static com.increff.pojo.ChannelTableGenerator.CHANNEL_ORDER_GENERATOR;
import static com.increff.pojo.ChannelTableGenerator.CHANNEL_ORDER_INITIAL_VALUE;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "channel_order")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelOrderId", "channelName" ,"clientName"})})
public class ChannelOrderPojo {
    @Id
    private Long id;
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false)
    private String channelName;
    private String channelOrderId;
}
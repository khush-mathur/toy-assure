package com.increff.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.increff.pojo.ChannelTableGenerator.CHANNEL_ORDER_ITEM_GENERATOR;
import static com.increff.pojo.ChannelTableGenerator.CHANNEL_ORDER_ITEM_INITIAL_VALUE;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "channel_order_item")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channSkuId", "orderId"})})
public class ChannelOrderItemPojo {
    @Id
    @TableGenerator(name = CHANNEL_ORDER_ITEM_GENERATOR,initialValue = CHANNEL_ORDER_ITEM_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = CHANNEL_ORDER_ITEM_GENERATOR)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private String channSkuId;
    @Column(nullable = false)
    private Long orderedQuantity;
    @Column(nullable = false,precision = 2)
    private Double sellingPricePerUnit;
}

package com.increff.pojo;

import lombok.*;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "assure_order_item")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId", "orderId"})})
public class OrderItemPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = ORDER_ITEM_GENERATOR,initialValue = ORDER_ITEM_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = ORDER_ITEM_GENERATOR)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private Long globalSkuId;
    @Column(nullable = false)
    private Long orderedQuantity;
    @Column(nullable = false)
    private Long allocatedQuantity;
    @Column(nullable = false)
    private Long fulfilledQuantity;
    @Column(nullable = false,precision = 2)
    private Double sellingPricePerUnit;
}

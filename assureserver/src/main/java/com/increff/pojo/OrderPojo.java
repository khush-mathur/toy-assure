package com.increff.pojo;

import com.increff.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.ORDER_GENERATOR;
import static com.increff.pojo.TableGenerator.ORDER_INITIAL_VALUE;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "assure_order")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelOrderId", "channelId" ,"clientId"})})
public class OrderPojo {
    @Id
    @TableGenerator(name = ORDER_GENERATOR,initialValue = ORDER_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = ORDER_GENERATOR)
    private Long id;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private Long customerId;
    @Column(nullable = false)
    private Long channelId;
    private String channelOrderId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}

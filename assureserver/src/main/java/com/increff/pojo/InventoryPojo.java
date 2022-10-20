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
@Entity(name = "assure_inventory")
public class InventoryPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = INVENTORY_GENERATOR,initialValue = INVENTORY_INITIAL_VALUE)
    @GeneratedValue(strategy= GenerationType.TABLE,generator = INVENTORY_GENERATOR)
    private Long id;
    @Column(unique = true ,nullable = false)
    private Long globalSkuId;
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}

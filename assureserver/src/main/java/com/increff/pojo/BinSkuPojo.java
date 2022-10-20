package com.increff.pojo;

import lombok.*;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "assure_bin_sku")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"binId", "globalSkuId"})})
public class BinSkuPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = BIN_SKU_GENERATOR,initialValue = BIN_SKU_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = BIN_SKU_GENERATOR)
    private Long id;
    @Column(nullable = false)
    private Long binId;
    private Long quantity;
    @Column(nullable = false)
    private Long globalSkuId;
}

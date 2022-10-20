package com.increff.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "assure_product")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId", "clientId"})})
public class ProductPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = PRODUCT_GENERATOR,initialValue = PRODUCT_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = PRODUCT_GENERATOR)
    private Long globalSkuId;
    @Column(nullable = false)
    private String clientSkuId;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false, precision = 2)
    private Double mrp;
    @Column(nullable = false)
    private String description;
}

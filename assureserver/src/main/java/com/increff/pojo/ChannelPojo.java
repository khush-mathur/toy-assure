package com.increff.pojo;

import com.increff.enums.InvoiceType;
import lombok.*;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "assure_channel")
public class ChannelPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = CHANNEL_GENERATOR,initialValue = CHANNEL_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = CHANNEL_GENERATOR)
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
}

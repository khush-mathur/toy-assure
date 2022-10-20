package com.increff.pojo;

import lombok.*;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "assure_channel_listing")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelSkuId", "clientId","channelId"})})
public class ChannelListingPojo {
    @Id
    @TableGenerator(name = CHANNEL_LISTING_GENERATOR,initialValue = CHANNEL_LISTING_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = CHANNEL_LISTING_GENERATOR)
    private Long id;
    @Column(nullable = false)
    private Long channelId;
    @Column(nullable = false)
    private String channelSkuId;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private Long globalSkuId;
}

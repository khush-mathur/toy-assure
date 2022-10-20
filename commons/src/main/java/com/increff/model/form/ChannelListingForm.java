package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelListingForm {
    private String channelSkuId;
    private String clientSkuId;
}

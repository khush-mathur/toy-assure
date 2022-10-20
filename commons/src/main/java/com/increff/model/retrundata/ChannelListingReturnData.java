package com.increff.model.retrundata;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelListingReturnData {
    private String channelSku;
    private String channelName;
    private String clientName;
    private String log;
}

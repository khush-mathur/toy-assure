package com.increff.model.data;

import com.increff.model.form.ChannelListingForm;
import lombok.*;

@Getter
@Setter
public class ChannelListingData extends ChannelListingForm {
    Long id;
    String channelName;
    String clientName;
}

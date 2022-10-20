package com.increff.dto.helper;

import com.increff.model.data.ChannelListingData;
import com.increff.model.form.ChannelListingForm;
import com.increff.pojo.ChannelListingPojo;

public class ChannelListingDtoHelper {
    public static ChannelListingData convertToData(ChannelListingPojo pojo) {
        ChannelListingData listingData = new ChannelListingData();
        listingData.setChannelSkuId(pojo.getChannelSkuId());
        listingData.setId(pojo.getId());
        return listingData;
    }

    public static ChannelListingPojo convertToPojo(ChannelListingForm form) {
        ChannelListingPojo listingPojo = new ChannelListingPojo();
        listingPojo.setChannelSkuId(form.getChannelSkuId());
        return listingPojo;
    }

}

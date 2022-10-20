package com.increff.dto.helper;

import com.increff.enums.InvoiceType;
import com.increff.model.data.ChannelData;
import com.increff.model.form.ChannelForm;
import com.increff.pojo.ChannelPojo;

public class ChannelDtoHelper {
    public static ChannelData convertToData(ChannelPojo pojo){
        ChannelData channel = new ChannelData();
        channel.setId(pojo.getId());
        channel.setName(pojo.getName());
        channel.setInvoiceType(pojo.getInvoiceType().toString());
        return channel;
    }

    public static ChannelPojo convertToPojo(ChannelForm form) {
        ChannelPojo pojo = new ChannelPojo();
        pojo.setName(form.getName());
        pojo.setInvoiceType(InvoiceType.valueOf(form.getInvoiceType()));
        return pojo;
    }
}

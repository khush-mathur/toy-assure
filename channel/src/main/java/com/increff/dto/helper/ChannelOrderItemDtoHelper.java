package com.increff.dto.helper;

import com.increff.model.data.ChannelOrderItemData;
import com.increff.model.data.OrderItemData;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.pojo.ChannelOrderItemPojo;

public class ChannelOrderItemDtoHelper {
    public static ChannelOrderItemPojo convertToPojo(ChannOrderCsvForm form) {
        ChannelOrderItemPojo pojo = new ChannelOrderItemPojo();
        pojo.setOrderedQuantity(form.getOrderQuantity());
        pojo.setChannSkuId(form.getChannSkuId());
        pojo.setSellingPricePerUnit(Double.parseDouble(form.getSellingPrice()));
        return pojo;
    }

    public static ChannelOrderItemData convertToData(ChannelOrderItemPojo pojo,String channel,String client,Long orderId) {
        ChannelOrderItemData itemData = new ChannelOrderItemData();
        itemData.setOrderedQuantity(pojo.getOrderedQuantity());
        itemData.setChannSku(pojo.getChannSkuId());
        itemData.setSellingPricePerUnit(pojo.getSellingPricePerUnit());
        itemData.setId(pojo.getId());
        itemData.setChannel(channel);
        itemData.setClient(client);
        itemData.setOrderId(orderId);
        return itemData;
    }
}

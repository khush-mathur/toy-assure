package com.increff.dto.helper;

import com.increff.enums.OrderStatus;
import com.increff.model.data.OrderData;
import com.increff.model.form.ChannOrderForm;
import com.increff.pojo.OrderPojo;

public class OrderDtoHelper {

    public static OrderPojo convertToPojo(ChannOrderForm form) {
        OrderPojo pojo = new OrderPojo();
        pojo.setStatus(OrderStatus.CREATED);
        pojo.setChannelOrderId(form.getChanOrderId());
        return pojo;
    }

    public static OrderData convertToData(OrderPojo pojo) {
        OrderData order = new OrderData();
        order.setId(pojo.getId());
        order.setStatus(pojo.getStatus());
        order.setChannelOrderId(pojo.getChannelOrderId());
        return order;
    }
    public static OrderPojo convertToPojo(Long clientId,Long customerId,Long channelId) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setClientId(clientId);
        orderPojo.setCustomerId(customerId);
        orderPojo.setChannelId(channelId);
        orderPojo.setStatus(OrderStatus.CREATED);
        orderPojo.setChannelOrderId("");
        return orderPojo;
    }
}

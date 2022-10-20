package com.increff.dto.helper;

import com.increff.model.data.ChannelOrderData;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.model.form.ChannOrderForm;
import com.increff.model.form.OrderCsvForm;
import com.increff.pojo.ChannelOrderPojo;

import java.util.ArrayList;
import java.util.List;

public class ChannelOrderDtoHelper {

    public static ChannelOrderPojo convertToPojo(ChannOrderForm orderForm) {
        ChannelOrderPojo pojo = new ChannelOrderPojo();
        pojo.setChannelName(orderForm.getChannelName());
        pojo.setCustomerName(orderForm.getCustomerName());
        pojo.setClientName(orderForm.getClientName());
        pojo.setChannelOrderId(orderForm.getChanOrderId());
        return pojo;
    }

    public static ChannelOrderData convertToData(ChannelOrderPojo pojo) {
        ChannelOrderData orderData = new ChannelOrderData();
        orderData.setClientName(pojo.getClientName());
        orderData.setCustomerName(pojo.getCustomerName());
        orderData.setChannelName(pojo.getChannelName());
        orderData.setChannelOrderId(pojo.getChannelOrderId());
        orderData.setId(pojo.getId());
        return orderData;
    }
}

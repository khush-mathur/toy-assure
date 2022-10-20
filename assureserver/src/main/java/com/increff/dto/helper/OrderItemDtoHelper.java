package com.increff.dto.helper;

import com.increff.model.data.OrderItemData;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.model.form.OrderCsvForm;
import com.increff.pojo.OrderItemPojo;

public class OrderItemDtoHelper {

    public static OrderItemPojo convertToPojo(OrderCsvForm csvForm) {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderedQuantity(csvForm.getOrderQuantity());
        orderItem.setAllocatedQuantity(0L);
        orderItem.setFulfilledQuantity(0L);
        orderItem.setSellingPricePerUnit(Double.valueOf(csvForm.getSellingPrice()));
        return orderItem;
    }
    public static OrderItemPojo convertToPojo(ChannOrderCsvForm csvForm) {
        OrderItemPojo orderItem = new OrderItemPojo();
        orderItem.setOrderedQuantity(csvForm.getOrderQuantity());
        orderItem.setAllocatedQuantity(0L);
        orderItem.setFulfilledQuantity(0L);
        orderItem.setSellingPricePerUnit(Double.valueOf(csvForm.getSellingPrice()));
        return orderItem;
    }
    public static OrderItemData convertToData(OrderItemPojo pojo) {
        OrderItemData orderItem = new OrderItemData();
        orderItem.setOrderId(pojo.getOrderId());
        orderItem.setId(pojo.getId());
        orderItem.setOrderedQuantity(pojo.getOrderedQuantity());
        orderItem.setFulfilledQuantity(pojo.getFulfilledQuantity());
        orderItem.setAllocatedQuantity(pojo.getAllocatedQuantity());
        orderItem.setSellingPricePerUnit(pojo.getSellingPricePerUnit());
        return orderItem;
    }
}

package com.increff.dto;

import com.increff.dto.helper.ChannelOrderDtoHelper;
import com.increff.dto.helper.ChannelOrderItemDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.*;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.model.form.ChannOrderForm;
import com.increff.pojo.ChannelOrderItemPojo;
import com.increff.pojo.ChannelOrderPojo;
import com.increff.rest.RestCall;
import com.increff.service.ChannelOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelOrderDto {
    @Autowired
    private ChannelOrderService channelOrderService;

    public File generateInvoice(Long orderId) throws ApiException {
        ChannelOrderPojo order = channelOrderService.getById(orderId);
        if(order == null)
            throw new ApiException("No such order exists");
        List<ChannelOrderItemData> orderItems = new ArrayList<>();
        for(ChannelOrderItemPojo pojo : channelOrderService.getItemsByOrderId(orderId)){
            orderItems.add(
                    ChannelOrderItemDtoHelper.convertToData(pojo,order.getChannelName(),order.getClientName(),orderId) );
        }
        ChannelInvoiceData invoiceData = new ChannelInvoiceData().builder()
                .orderId(orderId)
                .invoiceTime(ZonedDateTime.now().toString())
                .channelName(order.getChannelName())
                .clientName(order.getClientName())
                .orderItems(orderItems)
                .total(orderItems.stream().mapToDouble(orderItem ->
                        orderItem.getOrderedQuantity() * orderItem.getSellingPricePerUnit()).sum()).build();
        return channelOrderService.generateInvoice(invoiceData);
    }

    public ChannelOrderData createOrder(ChannOrderForm channOrderForm) throws IOException, ApiException {
        OrderData orderData = new RestCall().createOrder(channOrderForm,"http://localhost:9010/assure/order/create-channel-order", HttpMethod.POST);
        if(orderData == null)
            throw new ApiException("Invalid input");
        ChannelOrderPojo order = ChannelOrderDtoHelper.convertToPojo(channOrderForm);
        order.setId(orderData.getId());
        channelOrderService.addOrder(order);
        for (ChannOrderCsvForm orderForm : channOrderForm.getOrderItems()) {
            ChannelOrderItemPojo orderItemPojo = ChannelOrderItemDtoHelper.convertToPojo(normalise(orderForm));
            orderItemPojo.setOrderId(order.getId());
            channelOrderService.addOrderItem(orderItemPojo);
        }
        return ChannelOrderDtoHelper.convertToData(order);
    }

    private ChannOrderCsvForm normalise(ChannOrderCsvForm orderForm) {
        orderForm.setChannSkuId(orderForm.getChannSkuId().trim().toLowerCase());
        orderForm.setSellingPrice(String.valueOf((Math.round(Double.parseDouble(orderForm.getSellingPrice()) * 100.0) / 100.0)));
        return orderForm;
    }

    public List<ChannelOrderData> getAll() {
        List<ChannelOrderData> orderList = new ArrayList<>();
        for (ChannelOrderPojo pojo : channelOrderService.getAll()){
            orderList.add(ChannelOrderDtoHelper.convertToData(pojo));
        }
        return orderList;
    }

    public List<ChannelOrderItemData> getOrderItems(Long orderId) {
        ChannelOrderPojo order= channelOrderService.getById(orderId);
        List<ChannelOrderItemData> orderItemList = new ArrayList<>();
        for(ChannelOrderItemPojo pojo : channelOrderService.getItemsByOrderId(orderId)){
            orderItemList.add(
                    ChannelOrderItemDtoHelper.convertToData(pojo,order.getChannelName(),order.getClientName(),orderId) );
        }
        return orderItemList;
    }
}

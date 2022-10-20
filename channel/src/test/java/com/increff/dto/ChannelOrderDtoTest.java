package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.enums.OrderStatus;
import com.increff.exception.ApiException;
import com.increff.model.data.*;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.model.form.ChannOrderForm;
import com.increff.model.form.OrderCsvForm;
import com.increff.pojo.ChannelOrderPojo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChannelOrderDtoTest extends AbstractUnitTest {

    @Resource
    ChannelOrderDto channelOrderDto;

    @Test
    public void orderCreate() throws ApiException, IOException {
        String clientName = "flipkart";
        String customerName = "customer";
        String channelName = "channel";
        String channSku = "amazsku1";
        String channOrderId = RandomStringUtils.random(4);
        Long quantity = 4L;
        String sellingPrice = Double.valueOf(45.65).toString();
        List<ChannOrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(new ChannOrderCsvForm().builder().channSkuId(channSku).orderQuantity(quantity).sellingPrice(sellingPrice).build());

        ChannelOrderData orderData = channelOrderDto.createOrder( new ChannOrderForm().builder()
                .clientName(clientName).customerName(customerName)
                .channelName(channelName).chanOrderId(channOrderId)
                .orderItems(orderForm).build());
        ChannelOrderItemData orderItem = channelOrderDto.getOrderItems(orderData.getId()).get(0);

        assertEquals(channelName,orderData.getChannelName());
        assertEquals(clientName,orderData.getClientName());
        assertEquals(customerName,orderData.getCustomerName());
        assertEquals(channSku.trim().toLowerCase(),orderItem.getChannSku());
        assertEquals(quantity,orderItem.getOrderedQuantity());
        assertEquals(clientName,orderItem.getClient());
    }

    @Test
    public void getAll() throws IOException, ApiException {
        for(int i=0;i<2;i++) {
            String clientName = "flipkart";
            String customerName = "customer";
            String channelName = "channel";
            String channSku = "amazsku1";
            String channOrderId = RandomStringUtils.random(7);
            Long quantity = 4L;
            String sellingPrice = Double.valueOf(45.65).toString();
            List<ChannOrderCsvForm> orderForm = new ArrayList<>();
            orderForm.add(new ChannOrderCsvForm().builder().channSkuId(channSku).orderQuantity(quantity).sellingPrice(sellingPrice).build());

            ChannelOrderData orderData = channelOrderDto.createOrder(new ChannOrderForm().builder()
                    .clientName(clientName).customerName(customerName)
                    .channelName(channelName).chanOrderId(channOrderId)
                    .orderItems(orderForm).build());
        }
        assertEquals(2,channelOrderDto.getAll().size());
    }

    @Test
    public void generateInvoice() throws ApiException, IOException {
        String clientName = "flipkart";
        String customerName = "customer";
        String channelName = "channel";
        String channSku = "amazsku1";
        String channOrderId = RandomStringUtils.random(4);
        Long quantity = 4L;
        String sellingPrice = Double.valueOf(45.65).toString();
        List<ChannOrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(new ChannOrderCsvForm().builder().channSkuId(channSku).orderQuantity(quantity).sellingPrice(sellingPrice).build());

        ChannelOrderData orderData = channelOrderDto.createOrder( new ChannOrderForm().builder()
                .clientName(clientName).customerName(customerName)
                .channelName(channelName).chanOrderId(channOrderId)
                .orderItems(orderForm).build());

        File invoice = channelOrderDto.generateInvoice(orderData.getId());
        assertEquals("invoice" + orderData.getId() + ".pdf",invoice.getName());
    }

}

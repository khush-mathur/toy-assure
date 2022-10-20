package com.increff.controller;

import com.google.common.io.ByteStreams;
import com.increff.dto.ChannelOrderDto;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelOrderData;
import com.increff.model.data.ChannelOrderItemData;
import com.increff.model.data.InvoiceData;
import com.increff.model.data.OrderData;
import com.increff.model.form.ChannOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/channel-page")
public class ChannelPageController {
    @Autowired
    ChannelOrderDto channelOrderDto;

    @ApiOperation(value = "Generates invoice from channel")
    @RequestMapping(path = "/get-invoice/{orderId}", method = RequestMethod.GET)
    public File getInvoice(@PathVariable Long orderId) throws ApiException {
        return channelOrderDto.generateInvoice(orderId);
    }
    @ApiOperation(value = "Generates invoice from channel")
    @RequestMapping(path = "/generate-invoice/{orderId}", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable Long orderId, HttpServletResponse response) throws ApiException {
        try {
            File document = channelOrderDto.generateInvoice(orderId);
            InputStream is = new FileInputStream(document);
            ByteStreams.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException e) {
            throw new ApiException("Error while invoice generation");
        }
    }

    @ApiOperation(value = "Creates a order from channel")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ChannelOrderData create(@RequestBody ChannOrderForm channOrderForm) throws ApiException, IOException {
        return channelOrderDto.createOrder(channOrderForm);
    }

    @ApiOperation(value ="Fetches all the channel orders")
    @RequestMapping(path = "/viewAll",method = RequestMethod.GET)
    public List<ChannelOrderData> getAll() throws ApiException{
        return channelOrderDto.getAll();
    }
    @ApiOperation(value ="Fetches all the channel orders")
    @RequestMapping(path = "/viewOrder/{orderId}",method = RequestMethod.GET)
    public List<ChannelOrderItemData> getOrder(@PathVariable Long orderId) throws ApiException{
        return channelOrderDto.getOrderItems(orderId);
    }
}

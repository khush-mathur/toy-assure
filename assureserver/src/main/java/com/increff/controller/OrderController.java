package com.increff.controller;

import com.google.common.io.ByteStreams;
import com.increff.dto.OrderDto;
import com.increff.exception.ApiException;
import com.increff.model.data.OrderData;
import com.increff.model.data.OrderItemData;
import com.increff.model.form.OrderCsvForm;
import com.increff.model.form.ChannOrderForm;
import com.increff.service.OrderService;
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
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Creates a order")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public void create(@RequestParam Long clientId,@RequestParam Long customerId,@RequestBody List<OrderCsvForm> orderDetails) throws ApiException {
        orderDto.create(clientId,customerId,orderDetails);
    }
    @ApiOperation(value = "Creates a order from channel")
    @RequestMapping(path = "/create-channel-order", method = RequestMethod.POST)
    public OrderData createChannelOrder(@RequestBody ChannOrderForm orderDetails, HttpServletResponse response) throws IOException {
        OrderData order;
        try{
            order = orderDto.createChannelOrder(orderDetails);
        }
        catch(ApiException e){
            response.sendError(500,e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }
        return order;
    }

    @ApiOperation(value = "Allocates order")
    @RequestMapping(path = "/allocate/{orderId}", method = RequestMethod.POST)
    public List<OrderItemData> allocate(@PathVariable Long orderId) throws ApiException {
        return orderDto.allocate(orderId);
    }

    @ApiOperation(value = "Fulfill the order")
    @RequestMapping(path = "/fullfill/{orderId}", method = RequestMethod.POST)
    public List<OrderItemData> fullFill(@PathVariable Long orderId) throws ApiException {
        List<OrderItemData> orderDetails = orderDto.fullFill(orderId);
        return orderDetails;
    }

    @ApiOperation(value = "Generates invoice for the order")
    @RequestMapping(path = "/generate-invoice/{orderId}", method = RequestMethod.GET)
    public void generateInvoice(@PathVariable Long orderId, HttpServletResponse response) throws ApiException, IOException {
        try {
            File document = orderDto.generateInvoice(orderId);
            InputStream is = new FileInputStream(document);
            ByteStreams.copy(is, response.getOutputStream());
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException e) {
            throw new ApiException("Error while invoice generation");
        }
    }

    @ApiOperation(value ="Fetches all the orders")
    @RequestMapping(path = "/viewAll",method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException{
        return orderDto.getAll();
    }

    @ApiOperation(value ="Fetches item of order")
    @RequestMapping(path = "/viewOrder/{orderId}",method = RequestMethod.GET)
    public List<OrderItemData> getOrder(@PathVariable Long orderId) throws ApiException{
        return orderDto.getOrder(orderId);
    }
}

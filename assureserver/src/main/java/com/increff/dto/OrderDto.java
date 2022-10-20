package com.increff.dto;

import com.google.common.primitives.Doubles;
import com.increff.dto.helper.OrderDtoHelper;
import com.increff.dto.helper.OrderItemDtoHelper;
import com.increff.enums.InvoiceType;
import com.increff.enums.OrderStatus;
import com.increff.exception.ApiException;
import com.increff.model.data.InvoiceData;
import com.increff.model.data.OrderData;
import com.increff.model.data.OrderItemData;
import com.increff.model.form.BinSkuForm;
import com.increff.model.form.ChannOrderCsvForm;
import com.increff.model.form.ChannOrderForm;
import com.increff.model.form.OrderCsvForm;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.OrderItemPojo;
import com.increff.pojo.OrderPojo;
import com.increff.pojo.ProductPojo;
import com.increff.rest.RestCall;
import com.increff.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Component
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BinSkuService binSkuService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private ChannelListingService channelListingService;

    public void create(Long clientId,Long customerId,List<OrderCsvForm> orderItemList) throws ApiException {
        if(!validateOrderInput(clientId,customerId) || !validateUniqueClSku(orderItemList)) {
            throw new ApiException("Invalid Input");
        }
        Long channelId = channelService.getByName("internal").getId();
        OrderPojo order = orderService.create(OrderDtoHelper.convertToPojo(clientId,customerId,channelId));
        order.setChannelOrderId(order.getId().toString());
        for (OrderCsvForm rawOrderForm : orderItemList) {
            OrderCsvForm orderForm = normalise(rawOrderForm);
            if (!validateFormInput(clientId,orderForm))
                throw new ApiException("Invalid Input");
            OrderItemPojo orderItemPojo = OrderItemDtoHelper.convertToPojo(orderForm);
            orderItemPojo.setOrderId(order.getId());
            orderItemPojo.setGlobalSkuId(productService.getProductByClientIdNSkuId(order.getClientId(),orderForm.getClientSkuId()).getGlobalSkuId());
            orderItemService.add(orderItemPojo);
        }
    }
    public List<OrderItemData> allocate(Long orderId) throws ApiException {
        OrderPojo order = orderService.getById(orderId);
        if(!order.getStatus().equals(OrderStatus.CREATED))
            throw new ApiException("Invalid Order Status");
        boolean allocated = true;
        for(OrderItemPojo orderItem : orderItemService.getByOrderId(orderId)){
            Long allocatedQuantity = inventoryService.allocate(orderItem);
            binSkuService.allocate(orderItem,allocatedQuantity);
            OrderItemPojo updatedOrder = orderItemService.allocate(orderItem,allocatedQuantity);
            if(!updatedOrder.getOrderedQuantity().equals(updatedOrder.getAllocatedQuantity()))
                allocated = false;
        }
        if(allocated)
            orderService.updateStatus(orderId,OrderStatus.ALLOCATED);
        return getOrder(orderId);
    }
    public List<OrderItemData> fullFill(Long orderId) throws ApiException {
        OrderPojo order = orderService.getById(orderId);
        if(!order.getStatus().equals(OrderStatus.ALLOCATED))
            throw new ApiException("Unable to fullFill order which has not been allocated");
        for(OrderItemPojo orderItem : orderItemService.getByOrderId(orderId)){
            inventoryService.fulFill(orderItem);
            orderItemService.fulFill(orderItem);
        }
        orderService.updateStatus(orderId,OrderStatus.FULLFILLED);
        return getOrder(orderId);
    }
    public File generateInvoice(Long orderId) throws ApiException, IOException {
        OrderPojo order = orderService.getById(orderId);
        if(!order.getStatus().equals(OrderStatus.FULLFILLED))
            throw new ApiException("Unable to generate invoice of pending order");
        List<OrderItemData> orderItems = new ArrayList<>();
        for(OrderItemPojo pojo : orderItemService.getByOrderId(orderId)){
            OrderItemData itemData = OrderItemDtoHelper.convertToData(pojo);
            ProductPojo product = productService.getProductByGSkuId(pojo.getGlobalSkuId());
            itemData.setClSku(product.getClientSkuId());
            itemData.setClient(userService.getById(product.getClientId()).getName());
            orderItems.add(itemData);
        }
        InvoiceData invoiceData = InvoiceData.builder()
                .orderId(order.getId())
                .invoiceTime(ZonedDateTime.now().toString())
                .orderItems(orderItems)
                .clientName(userService.getById(order.getClientId()).getName())
                .total(orderItems.stream().mapToDouble(orderItem ->
                                orderItem.getOrderedQuantity() * orderItem.getSellingPricePerUnit()).sum()).build();

        if(channelService.getById(order.getChannelId()).getInvoiceType().equals(InvoiceType.CHANNEL)) {
            String url = "http://localhost:9020/channel/channel-page/get-invoice/"+orderId;
            return new RestCall().getChannelInvoice(url, HttpMethod.GET);
        }
        else
            return invoiceService.generateInvoice(invoiceData);
    }

    public List<OrderData> getAll() throws ApiException {
        List<OrderData> orderList = new ArrayList<>();
        for (OrderPojo pojo : orderService.getAll()){
            OrderData orderData = OrderDtoHelper.convertToData(pojo);
            orderData.setClientName(userService.getById(pojo.getClientId()).getName());
            orderData.setCustomerName(userService.getById(pojo.getCustomerId()).getName());
            orderData.setChannelName(channelService.getById(pojo.getChannelId()).getName());
            orderList.add(orderData);
        }
        return orderList;
    }
    public List<OrderItemData> getOrder(Long orderId) throws ApiException {
        orderService.getById(orderId);
        List<OrderItemData> orderItemList = new ArrayList<>();
        for(OrderItemPojo pojo : orderItemService.getByOrderId(orderId)){
            OrderItemData itemData = OrderItemDtoHelper.convertToData(pojo);
            ProductPojo product = productService.getProductByGSkuId(pojo.getGlobalSkuId());
            itemData.setClSku(product.getClientSkuId());
            itemData.setClient(userService.getById(product.getClientId()).getName());
            orderItemList.add(itemData);
        }
        return orderItemList;
    }
    public OrderData createChannelOrder(ChannOrderForm rawOrderDetail) throws ApiException {
        ChannOrderForm orderDetails = normalise(rawOrderDetail);
        if(!validateChannOrderInput(orderDetails) || !validateUniqueChanSku(orderDetails.getOrderItems())) {
            throw new ApiException("Invalid Input");
        }
        OrderPojo orderPojo  = OrderDtoHelper.convertToPojo(orderDetails);
        orderPojo.setChannelId(channelService.getByName(orderDetails.getChannelName()).getId());
        orderPojo.setClientId(userService.getByName(orderDetails.getClientName()).getId());
        orderPojo.setCustomerId(userService.getByName(orderDetails.getCustomerName()).getId());
        OrderPojo order = orderService.create(orderPojo);
        for (ChannOrderCsvForm rawOrderForm : orderDetails.getOrderItems()) {
            ChannOrderCsvForm orderCsvForm = normalise(rawOrderForm);
            if (!validateChannFormInput(order.getChannelId(),order.getClientId(), orderCsvForm))
                throw new ApiException("Invalid Input");
            OrderItemPojo orderItemPojo = OrderItemDtoHelper.convertToPojo(orderCsvForm);
            orderItemPojo.setOrderId(order.getId());
            Long globalSkuId = channelListingService.getByChanClientAndSkuId(order.getChannelId(),order.getClientId(), orderCsvForm.getChannSkuId()).getGlobalSkuId();
            orderItemPojo.setGlobalSkuId(globalSkuId);
            orderItemService.add(orderItemPojo);
        }
        OrderData orderData = OrderDtoHelper.convertToData(order);
        orderData.setClientName(userService.getById(order.getClientId()).getName());
        orderData.setCustomerName(userService.getById(order.getCustomerId()).getName());
        orderData.setChannelName(channelService.getById(order.getChannelId()).getName());
        return orderData;
    }
    private ChannOrderCsvForm normalise(ChannOrderCsvForm order) {
        order.setChannSkuId(order.getChannSkuId().trim().toLowerCase());
        order.setSellingPrice(String.valueOf((Math.round(Double.parseDouble(order.getSellingPrice()) * 100.0) / 100.0)));
        return order;
    }
    private ChannOrderForm normalise(ChannOrderForm order){
        order.setChanOrderId(order.getChanOrderId().trim().toLowerCase());
        order.setChannelName(order.getChannelName().trim().toLowerCase());
        return order;
    }
    private OrderCsvForm normalise(OrderCsvForm order){
        order.setClientSkuId(order.getClientSkuId().trim().toLowerCase());
        order.setSellingPrice(String.valueOf((Math.round(Double.parseDouble(order.getSellingPrice()) * 100.0) / 100.0)));
        return order;
    }
    private boolean validateFormInput(Long clientId, OrderCsvForm form) throws ApiException {
        if(form.getClientSkuId()==null || form.getClientSkuId().isEmpty() || productService.getProductByClientIdNSkuId(clientId,form.getClientSkuId())==null)
            throw new ApiException("Invalid Input : Enter valid clientSku-ID ");
        if(form.getSellingPrice()==null || form.getSellingPrice().isEmpty()
                || Doubles.tryParse(form.getSellingPrice())==null || Double.parseDouble(form.getSellingPrice()) <=0)
            throw new ApiException("Invalid Input : Enter valid selling price");
        if(form.getOrderQuantity()==null || form.getOrderQuantity()<=0)
            throw new ApiException("Invalid Input : Enter valid Quantity");
        return true;
    }
    private boolean validateChannOrderInput(ChannOrderForm orderDetails) throws ApiException {
        ChannelPojo channel  = channelService.getByName(orderDetails.getChannelName());
        if(orderDetails.getChannelName()==null || isNull(channel))
            throw new ApiException("Invalid Input : Enter valid channel name");
        if(orderDetails.getChanOrderId()==null)
            throw new ApiException("Invalid Input : Enter valid channel-order ID");
        if(orderService.getByChannOrderId(channel.getId(),orderDetails.getChanOrderId())!=null)
            throw new ApiException("Invalid Input : order-Id already exists");
        if(orderDetails.getClientName()==null || !userService.isClient(orderDetails.getClientName()))
            throw new ApiException("Invalid Input : Enter valid Client");
        if(orderDetails.getCustomerName()==null || !userService.isCustomer(orderDetails.getCustomerName()))
            throw new ApiException("Invalid Input : Enter valid customer");
        return true;
    }
    private boolean validateUniqueClSku(List<OrderCsvForm> formList) throws ApiException {
        if(formList == null || formList.isEmpty())
            throw new ApiException("Enter valid csv form");
        List<String> skuList = new ArrayList<>();
        for(OrderCsvForm form : formList){
            String inputClintSku = form.getClientSkuId().trim().toLowerCase();
            if(skuList.contains(inputClintSku))
                throw new ApiException("Duplicate client Sku : " + form.getClientSkuId());
            skuList.add(inputClintSku);
        }
        return true;
    }
    private boolean validateUniqueChanSku(List<ChannOrderCsvForm> formList) throws ApiException {
        if(formList == null || formList.isEmpty())
            throw new ApiException("Enter valid csv form");
        List<String> skuList = new ArrayList<>();
        for(ChannOrderCsvForm form : formList){
            String inputClintSku = form.getChannSkuId().trim().toLowerCase();
            if(skuList.contains(inputClintSku))
                throw new ApiException("Duplicate client Sku : " + form.getChannSkuId());
            skuList.add(inputClintSku);
        }
        return true;
    }
    private boolean validateChannFormInput(Long channId,Long clientId, ChannOrderCsvForm form) throws ApiException {
        if(form.getChannSkuId()==null || form.getChannSkuId().isEmpty() || channelListingService.getByChanClientAndSkuId(channId,clientId,form.getChannSkuId())==null)
            throw new ApiException("Invalid Input : Enter valid channSku-ID ");
        if(form.getSellingPrice()==null || form.getSellingPrice().isEmpty()
                || Doubles.tryParse(form.getSellingPrice())==null || Double.parseDouble(form.getSellingPrice()) <=0)
            throw new ApiException("Invalid Input : Enter valid selling price");
        if(form.getOrderQuantity()==null || form.getOrderQuantity()<=0)
            throw new ApiException("Invalid Input : Enter valid Quantity");
        return true;
    }
    private boolean validateOrderInput(Long clientId,Long customerId) throws ApiException {
        if(clientId==null || !userService.isClient(clientId))
            throw new ApiException("Invalid Input : Enter valid Client");
        if(customerId==null || !userService.isCustomer(customerId))
            throw new ApiException("Invalid Input : Enter valid customer");
        return true;
    }
}

package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.enums.OrderStatus;
import com.increff.exception.ApiException;
import com.increff.model.data.*;
import com.increff.model.form.*;
import com.increff.model.retrundata.ChannelListingReturnData;
import com.increff.model.retrundata.ProductReturnData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class OrderDtoTest extends AbstractUnitTest {
    @Resource
    OrderDto orderDto;
    @Resource
    BinSkuDto binSkuDto;
    @Resource
    ProductDto productDto;
    @Resource
    UserDto userDto;
    @Resource
    BinDto binDto;
    @Resource
    ChannelDto channelDto;
    @Resource
    ChannelListingDto channelListingDto;

    @Test
    public void orderCreate() throws ApiException {
        UserData client = createClient();
        UserData customer = createCustomer();
        ChannelData internalChannel = createInternalChannel();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(createBin().getBinId(),client.getId(),clientSku,Math.abs(new Random().nextLong()));
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<OrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createOrderCsvForm(clientSku,quantity,sellingPrice));
        orderDto.create(client.getId(), customer.getId(), orderForm);
        OrderData orderData = orderDto.getAll().get(0);
        OrderItemData orderItem = orderDto.getOrder(orderData.getId()).get(0);
        assertEquals("internal",orderData.getChannelName());
        assertEquals(orderData.getId().toString(),orderData.getChannelOrderId());
        assertEquals(OrderStatus.CREATED,orderData.getStatus());
        assertEquals(client.getName(),orderData.getClientName());
        assertEquals(customer.getName(),orderData.getCustomerName());
        assertEquals(clientSku.trim().toLowerCase(),orderItem.getClSku());
        assertEquals(quantity,orderItem.getOrderedQuantity());
        assertEquals(client.getName(),orderItem.getClient());
    }
    @Test
    public void orderAllocate() throws ApiException {
        UserData client = createClient();
        UserData customer = createCustomer();
        Long binId1 = createBin().getBinId();
        ChannelData internalChannel = createInternalChannel();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(binId1,client.getId(),clientSku,3L);
        createInventory(createBin().getBinId(),client.getId(),clientSku,3L);
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<OrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createOrderCsvForm(clientSku,10L,sellingPrice));

        orderDto.create(client.getId(), customer.getId(), orderForm);
        OrderData createdOrderData = orderDto.getAll().get(0);
        OrderItemData postAllocateOrderItem = orderDto.allocate(createdOrderData.getId()).get(0);

        assertEquals(OrderStatus.CREATED,orderDto.getAll().get(0).getStatus());
        assertEquals(Long.valueOf(6L),postAllocateOrderItem.getAllocatedQuantity());
        assertEquals(Long.valueOf(0L),binSkuDto.fetchAll().get(0).getAvailableQuantity());
        assertEquals(Long.valueOf(0L),binSkuDto.fetchAllBinWise().get(1).getQuantity());

        createInventory(binId1,client.getId(),clientSku,6L);
        OrderItemData allocatedItem = orderDto.allocate(createdOrderData.getId()).get(0);

        assertEquals(OrderStatus.ALLOCATED,orderDto.getAll().get(0).getStatus());
        assertEquals(Long.valueOf(10L),allocatedItem.getAllocatedQuantity());
        assertEquals(Long.valueOf(2L),binSkuDto.fetchAll().get(0).getAvailableQuantity());
        assertEquals(Long.valueOf(2L),binSkuDto.fetchAllBinWise().get(0).getQuantity());
    }
    @Test
    public void orderFullfill() throws ApiException {
        UserData client = createClient();
        UserData customer = createCustomer();
        Long binId1 = createBin().getBinId();
        ChannelData internalChannel = createInternalChannel();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(binId1,client.getId(),clientSku,9L);
        createInventory(createBin().getBinId(),client.getId(),clientSku,3L);
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<OrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createOrderCsvForm(clientSku,10L,sellingPrice));

        orderDto.create(client.getId(), customer.getId(), orderForm);
        OrderData createdOrderData = orderDto.getAll().get(0);
        orderDto.allocate(createdOrderData.getId());
        OrderItemData fulfilledItem = orderDto.fullFill(createdOrderData.getId()).get(0);

        assertEquals(OrderStatus.FULLFILLED,orderDto.getAll().get(0).getStatus());
        assertEquals(Long.valueOf(0L),fulfilledItem.getAllocatedQuantity());
        assertEquals(Long.valueOf(10L),fulfilledItem.getFulfilledQuantity());
        assertEquals(Long.valueOf(2L),binSkuDto.fetchAll().get(0).getAvailableQuantity());
        assertEquals(Long.valueOf(2L),binSkuDto.fetchAllBinWise().get(1).getQuantity());

    }
    @Test(expected = ApiException.class)
    public void getInvalidOrder() throws ApiException {
        UserData client = createClient();
        UserData customer = createCustomer();
        ChannelData internalChannel = createInternalChannel();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(createBin().getBinId(),client.getId(),clientSku,Math.abs(new Random().nextLong()));
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<OrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createOrderCsvForm(clientSku,quantity,sellingPrice));
        orderDto.create(client.getId(), customer.getId(), orderForm);
        Long orderId = new Random().nextLong();
        try {
            orderDto.getOrder(orderId).get(0);
        }
        catch (ApiException e){
            assertEquals("No order with id: " + orderId + " exists.",e.getMessage());
            throw e;
        }
    }
    @Test
    public void channelOrderCreate() throws ApiException {
        UserData client = createClient();
        UserData customer = createCustomer();
        ChannelData channel = createChannel("CHANNEL");
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(createBin().getBinId(),client.getId(),clientSku,Math.abs(new Random().nextLong()));
        String channSku = createChannelListing(client.getName(),channel.getName(),RandomStringUtils.random(6),clientSku).getChannelSku();
        String channOrderId = RandomStringUtils.random(4);
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<ChannOrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createChannelOrderCsvForm(channSku,quantity,sellingPrice));

        OrderData orderData = orderDto.createChannelOrder( new ChannOrderForm().builder()
                .clientName(client.getName()).customerName(customer.getName())
                .channelName(channel.getName()).chanOrderId(channOrderId)
                .orderItems(orderForm).build());
        OrderItemData orderItem = orderDto.getOrder(orderData.getId()).get(0);

        assertEquals(channel.getName(),orderData.getChannelName());
        assertEquals(OrderStatus.CREATED,orderData.getStatus());
        assertEquals(client.getName(),orderData.getClientName());
        assertEquals(customer.getName(),orderData.getCustomerName());
        assertEquals(clientSku.trim().toLowerCase(),orderItem.getClSku());
        assertEquals(quantity,orderItem.getOrderedQuantity());
        assertEquals(client.getName(),orderItem.getClient());
    }
    @Test
    public void generateInvoice() throws ApiException, IOException {
        UserData client = createClient();
        UserData customer = createCustomer();
        Long binId1 = createBin().getBinId();
        ChannelData internalChannel = createInternalChannel();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        createInventory(binId1,client.getId(),clientSku,9L);
        createInventory(createBin().getBinId(),client.getId(),clientSku,3L);
        Long quantity = Math.abs(new Random().nextLong());
        String sellingPrice = String.valueOf(new Random().nextDouble());
        List<OrderCsvForm> orderForm = new ArrayList<>();
        orderForm.add(createOrderCsvForm(clientSku,10L,sellingPrice));

        orderDto.create(client.getId(), customer.getId(), orderForm);
        OrderData createdOrderData = orderDto.getAll().get(0);
        orderDto.allocate(createdOrderData.getId());
        orderDto.fullFill(createdOrderData.getId()).get(0);
        File invoice = orderDto.generateInvoice(createdOrderData.getId());
        assertEquals("invoice"+ createdOrderData.getId()+".pdf",invoice.getName());
    }

    private OrderCsvForm createOrderCsvForm( String clientSku,Long quantity,String sellingPrice){
        return new OrderCsvForm().builder()
                .clientSkuId(clientSku).orderQuantity(quantity)
                .sellingPrice(sellingPrice).build();
    }

    private ChannOrderCsvForm createChannelOrderCsvForm( String channSku,Long quantity,String sellingPrice){
        return new ChannOrderCsvForm().builder()
                .channSkuId(channSku).orderQuantity(quantity)
                .sellingPrice(sellingPrice).build();
    }
    private ProductReturnData createProduct(Long clientId){
        List<ProductForm> list = new ArrayList<>();
        list.add(new ProductForm().builder()
                .name(RandomStringUtils.random(6)).brand(RandomStringUtils.random(6))
                .mrp(String.valueOf(new Random().nextDouble()))
                .description(RandomStringUtils.random(6)).clientSkuId(RandomStringUtils.random(6))
                .build());
        return productDto.create(clientId,list).get(0);
    }
    private UserData createClient() throws ApiException {
        return userDto.create ( new UserForm().builder().name(RandomStringUtils.random(6)).type("CLIENT").build());
    }
    private UserData createCustomer() throws ApiException {
        return userDto.create ( new UserForm().builder().name(RandomStringUtils.random(6)).type("CUSTOMER").build());
    }
    private BinData createBin() throws ApiException {
        return binDto.create (1L).get(0);
    }
    private void createInventory(Long binId,Long clientId,String clientSku,Long quantity) throws ApiException {
        List<BinSkuForm> binSkuForms = new ArrayList<>();
        binSkuForms.add( new BinSkuForm().builder().binId(binId)
                        .clientSkuId(clientSku).quantity(quantity).build() );
        binSkuDto.add(clientId,binSkuForms);
    }
    private ChannelData createChannel(String invoiceType) throws ApiException {
        return channelDto.create ( new ChannelForm().builder().name(RandomStringUtils.random(6)).invoiceType(invoiceType).build());
    }
    private ChannelData createInternalChannel() throws ApiException {
        return channelDto.create ( new ChannelForm().builder().name("internal").invoiceType("SELF").build());
    }

    private ChannelListingReturnData createChannelListing(String clientName, String channelName, String channelSku , String clientSku) throws ApiException {
        List<ChannelListingForm> form = new ArrayList<>();
        form.add(new ChannelListingForm().builder().
                channelSkuId(channelSku).clientSkuId(clientSku).build());
        return channelListingDto.create(clientName,channelName,form).get(0);

    }
}

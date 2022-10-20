package com.increff.service;

import com.increff.AbstractUnitTest;
import com.increff.enums.InvoiceType;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelPojo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChannelServiceTest extends AbstractUnitTest {

    @Resource
    ChannelService service;

    @Test
    public void getAll() {
        for(int i =0 ;i<2;i ++){
            insertHelper(RandomStringUtils.random(5),InvoiceType.CHANNEL);
        }
        for(int i =0 ;i<2;i ++){
            insertHelper(RandomStringUtils.random(5),InvoiceType.SELF);
        }
        List<ChannelPojo> skuList = service.getAll();
        assertEquals(4,skuList.size());
    }

    @Test
    public void channelCreate() throws ApiException {
        String channelName1 = RandomStringUtils.random(6);
        String channelName2 = RandomStringUtils.random(6);
        InvoiceType channelInvoice = InvoiceType.CHANNEL;
        InvoiceType selfInvoice = InvoiceType.SELF;
        ChannelPojo createdChannel = insertHelper(channelName1,channelInvoice);
        ChannelPojo createdChannelWSelf = insertHelper(channelName2,selfInvoice);
        assertEquals(channelName1,createdChannel.getName());
        assertEquals(channelName2,createdChannelWSelf.getName());
        assertEquals(channelInvoice,createdChannel.getInvoiceType());
        assertEquals(selfInvoice,createdChannelWSelf.getInvoiceType());
    }

    @Test
    public void getChannelById() throws ApiException {
        ChannelPojo channel = insertHelper(RandomStringUtils.random(6),InvoiceType.CHANNEL);
        ChannelPojo fetchedChannel = service.getById(channel.getId());
        assertEquals(channel,fetchedChannel);
    }

    @Test(expected = ApiException.class)
    public void getChannelByInvalidId() throws ApiException {
        insertHelper(RandomStringUtils.random(6),InvoiceType.CHANNEL);;
        try {
            service.getById(new Random().nextLong());
        }catch(ApiException e){
            assertEquals("No such channel exists.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void getChannelByName() throws ApiException {
        ChannelPojo channel = insertHelper(RandomStringUtils.random(6),InvoiceType.CHANNEL);
        ChannelPojo fetchedChannel = service.getByName(channel.getName());
        assertEquals(channel,fetchedChannel);
    }

    public ChannelPojo insertHelper(String  channelName, InvoiceType invoiceType){
        ChannelPojo channel = new ChannelPojo().builder()
                .name(channelName)
                .invoiceType(invoiceType)
                .build();
        return service.create(channel);
    }
}

package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.form.ChannelForm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChannelDtoTest extends AbstractUnitTest {
    @Resource
    ChannelDto channelDto;

    private final static String selfType = "SELF";
    private final static String channelType = "CHANNEL";

    @Test
    public void createChannel() throws ApiException {
        String channel1Name = RandomStringUtils.random(6);
        String channel2Name = RandomStringUtils.random(6);
        ChannelData channelSelf = channelDto.create(createChannelForm(channel1Name,selfType));
        ChannelData channel2 = channelDto.create(createChannelForm(channel2Name,channelType));
        assertEquals(channel1Name.trim().toLowerCase(),channelSelf.getName());
        assertEquals(channel2Name.trim().toLowerCase(),channel2.getName());
        assertEquals(selfType,channelSelf.getInvoiceType());
        assertEquals(channelType,channel2.getInvoiceType());
    }

    @Test(expected = ApiException.class)
    public void createExistingChannel() throws ApiException {
        String userName = RandomStringUtils.random(6);
        channelDto.create(createChannelForm(userName,channelType));
        try {
            channelDto.create(createChannelForm(userName, selfType));
        }
        catch(ApiException e){
            assertEquals("Channel already exists.",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void createWithInvalidName() throws ApiException {
        try {
            channelDto.create(createChannelForm("", selfType));
        }
        catch(ApiException e){
            assertEquals("Invalid Input : name cannot be empty",e.getMessage());
            throw e;
        }

    }

    @Test(expected = ApiException.class)
    public void createWithInvalidInvoiceType() throws ApiException {
        try {
            channelDto.create(createChannelForm(RandomStringUtils.random(6), "invoiceType"));
        }
        catch(ApiException e){
            assertEquals("Invalid input : Enter a valid Invoice Type",e.getMessage());
            throw e;
        }
    }
    @Test
    public void getById() throws ApiException {
        ChannelData createChannel = channelDto.create(createChannelForm(RandomStringUtils.random(6),channelType));
        ChannelData fetchedChannel = channelDto.getById(createChannel.getId());
        assertEquals(createChannel.getName(),fetchedChannel.getName());
        assertEquals(createChannel.getInvoiceType(),fetchedChannel.getInvoiceType());
    }

    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        channelDto.create(createChannelForm(RandomStringUtils.random(6),channelType));
        try {
            channelDto.getById(new Random().nextLong());
        }
        catch (ApiException e){
            assertEquals("No such channel exists.",e.getMessage());
            throw e;
        }
    }
    @Test
    public void getAll() throws ApiException {
        for(int i =0;i<3;i++){
            channelDto.create(createChannelForm(RandomStringUtils.random(6),selfType));
        }
        for(int i =0;i<4;i++){
            channelDto.create(createChannelForm(RandomStringUtils.random(6),channelType));
        }
        List<ChannelData> channelList = channelDto.fetchAll();
        assertEquals(7,channelList.size());
    }

    private ChannelForm createChannelForm(String name, String type){
        return new ChannelForm().builder().name(name).invoiceType(type).build();
    }
}

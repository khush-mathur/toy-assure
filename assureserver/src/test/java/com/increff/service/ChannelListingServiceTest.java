package com.increff.service;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelListingPojo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChannelListingServiceTest extends AbstractUnitTest {

    @Resource
    ChannelListingService service;

    @Test
    public void getAll() throws ApiException {
        for(int i =0 ;i<5;i ++){
            insertHelper(new Random().nextLong(),new Random().nextLong(), RandomStringUtils.random(5),new Random().nextLong());
        }
        List<ChannelListingPojo> skuList = service.getAll();
        assertEquals(5,skuList.size());
    }

    @Test
    public void channelListingCreate() throws ApiException {
        Long clientId = new Random().nextLong();
        Long channelId = new Random().nextLong();
        String channelSku = RandomStringUtils.random(6);
        Long globalSkuId = new Random().nextLong();
        ChannelListingPojo createdChannelListing = insertHelper(clientId,channelId,channelSku,globalSkuId);
        assertEquals(channelId,createdChannelListing.getChannelId());
        assertEquals(clientId,createdChannelListing.getClientId());
        assertEquals(channelSku,createdChannelListing.getChannelSkuId());
        assertEquals(globalSkuId,createdChannelListing.getGlobalSkuId());
    }

    @Test(expected = ApiException.class)
    public void addExistingListing() throws ApiException {
        ChannelListingPojo createdListing = insertHelper(new Random().nextLong(),new Random().nextLong(),RandomStringUtils.random(6),new Random().nextLong());
        try {
            insertHelper(createdListing.getClientId(), createdListing.getChannelId(), createdListing.getChannelSkuId(),new Random().nextLong());
        }catch(ApiException e){
            assertEquals("Listing with channel Sku Id "+ createdListing.getChannelSkuId()  + " already exists of channel " + createdListing.getChannelId(),e.getMessage());
            throw e;
        }
    }

    @Test
    public void getListingById() throws ApiException {
        ChannelListingPojo createdListing = insertHelper(new Random().nextLong(),new Random().nextLong(),RandomStringUtils.random(6),new Random().nextLong());
        ChannelListingPojo fetchedListing = service.getById(createdListing.getId());
        assertEquals(createdListing,fetchedListing);
    }
    @Test(expected = ApiException.class)
    public void getListingByInvalidId() throws ApiException {
        insertHelper(new Random().nextLong(),new Random().nextLong(),RandomStringUtils.random(6),new Random().nextLong());
        try {
            service.getById(new Random().nextLong());
        }catch(ApiException e){
            assertEquals("No such product exists.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void getListingByClChannNSku() throws ApiException {
        Long clientId = new Random().nextLong();
        Long channelId = new Random().nextLong();
        String channelSku = RandomStringUtils.random(6);
        ChannelListingPojo createdListing = insertHelper(clientId,channelId,channelSku,new Random().nextLong());
        ChannelListingPojo fetchedListing = service.getByChanClientAndSkuId(channelId,clientId,channelSku);
        assertEquals(createdListing,fetchedListing);
    }

    @Test(expected = ApiException.class)
    public void getListingByInvalidClChannNSku() throws ApiException {
        insertHelper(new Random().nextLong(),new Random().nextLong(),RandomStringUtils.random(6),new Random().nextLong());
        try {
            service.getByChanClientAndSkuId(new Random().nextLong(),new Random().nextLong(),RandomStringUtils.random(6));
        }catch(ApiException e){
            assertEquals("No such product exists.",e.getMessage());
            throw e;
        }
    }

    public ChannelListingPojo insertHelper(Long clientId,Long channelId, String channelSkuId, Long globalSkuId) throws ApiException {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo().builder()
                .channelId(channelId)
                .globalSkuId(globalSkuId)
                .channelSkuId(channelSkuId)
                .clientId(clientId)
                .build();
        return service.create(channelListingPojo);
    }
}

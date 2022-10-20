package com.increff.service;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.OrderItemPojo;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BinSkuServiceTest extends AbstractUnitTest {
    @Resource
    BinSkuService service;

    @Test
    public void binSkuCreate() {
        Long binId = new Random().nextLong();
        Long quantity = new Random().nextLong();
        Long globalSkuId = new Random().nextLong();
        BinSkuPojo createdBinSku = insertHelper(binId,quantity,globalSkuId);
        assertEquals(binId,createdBinSku.getBinId());
        assertEquals(quantity,createdBinSku.getQuantity());
        assertEquals(globalSkuId,createdBinSku.getGlobalSkuId());
    }

    @Test
    public void binSkuAddExisting() {
        Long binId = new Random().nextLong();
        Long quantity1 = new Random().nextLong();
        Long globalSkuId = new Random().nextLong();
        insertHelper(binId,quantity1,globalSkuId);
        Long quantity2 = new Random().nextLong();
        BinSkuPojo createdBinSku2 = insertHelper(binId,quantity2,globalSkuId);
        assertEquals(binId,createdBinSku2.getBinId());
        assertEquals(Long.valueOf(quantity1 + quantity2),createdBinSku2.getQuantity());
        assertEquals(globalSkuId,createdBinSku2.getGlobalSkuId());
    }

    @Test
    public void getAllBinWise(){
        for(int i =0 ;i<5;i ++){
            insertHelper(new Random().nextLong(),new Random().nextLong(),new Random().nextLong());
        }
        List<BinSkuPojo> skuList = service.getAllBinWise();
        assertEquals(5,skuList.size());
    }

    @Test
    public void binSkuGetBySkuID() {
        Long globalSkuId = new Random().nextLong();
        for(int i =0;i<3;i++){
            insertHelper(new Random().nextLong(),new Random().nextLong(),globalSkuId);
        }
        List<BinSkuPojo> skuList = service.getByGSkuId(globalSkuId);
        for(BinSkuPojo fetchedPojo :skuList){
            assertEquals(globalSkuId,fetchedPojo.getGlobalSkuId());
        }
    }

    @Test
    public void binSkuUpdate() {
        BinSkuPojo createdBinSku = insertHelper(new Random().nextLong(),new Random().nextLong(),new Random().nextLong());
        Long updatedQuantity = new Random().nextLong();
        BinSkuPojo inputBinSku = new BinSkuPojo().builder()
                        .quantity(updatedQuantity).build();
        BinSkuPojo updatedSku = service.update(createdBinSku.getId(),inputBinSku);
        assertEquals(updatedQuantity,updatedSku.getQuantity());
    }
    @Test
    public void binSkuGetByBinAndGSku() throws ApiException {
        Long binId = new Random().nextLong();
        Long globalSkuId = new Random().nextLong();
        BinSkuPojo createdBinSku = insertHelper(binId,new Random().nextLong(),globalSkuId);
        BinSkuPojo fetchedBinSku = service.get(binId,globalSkuId);
        assertEquals(createdBinSku,fetchedBinSku);
    }

    @Test(expected = ApiException.class)
    public void binSkuGetByInvalidBinAndGSku() throws ApiException {
        insertHelper(new Random().nextLong(),new Random().nextLong(),new Random().nextLong());
        try {
            service.get(new Random().nextLong(),new Random().nextLong());
        } catch (ApiException e) {
            assertEquals("No such sku in given bin exists.", e.getMessage());
            throw e;
        }
    }

    @Test
    public void binSkuGetById() throws ApiException {
        BinSkuPojo createdBinSku = insertHelper(new Random().nextLong(),new Random().nextLong(),new Random().nextLong());
        BinSkuPojo fetchedBinSku = service.getById(createdBinSku.getId());
        assertEquals(createdBinSku,fetchedBinSku);
    }

    @Test(expected = ApiException.class)
    public void binSkuGetByInvalidId() throws ApiException {
        insertHelper(new Random().nextLong(),new Random().nextLong(),new Random().nextLong());
        try {
            service.getById(new Random().nextLong());
        } catch (ApiException e) {
            assertEquals("No such bin-sku exists.",e.getMessage());
            throw e;
        }
    }
    @Test
    public void allocateQuantity(){
        Long globalSkuId = new Random().nextLong();
        OrderItemPojo orderItem = new OrderItemPojo().builder()
                .globalSkuId(globalSkuId).build();
        Long quantityToAllocate = 10L;
        insertHelper(new Random().nextLong(),7L,globalSkuId);
        insertHelper(new Random().nextLong(),23L,globalSkuId);
        service.allocate(orderItem,quantityToAllocate);
        Long postAllocationQuantity = service.getByGSkuId(globalSkuId)
                .stream().mapToLong(sku -> sku.getQuantity()).sum();
        assertEquals(Long.valueOf(20L),postAllocationQuantity);
    }

    public BinSkuPojo insertHelper(Long binId,Long quantity,Long globalSkuId){
        BinSkuPojo binSkuPojo = new BinSkuPojo().builder()
                .binId(binId)
                .quantity(quantity)
                .globalSkuId(globalSkuId)
                .build();
        return service.add(binSkuPojo);
    }
}

package com.increff.service;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.pojo.InventoryPojo;
import com.increff.pojo.OrderItemPojo;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest {

    @Resource
    InventoryService service;


    @Test
    public void inventoryAdd(){
        Long globalSkuId = new Random().nextLong();
        Long quantity = new Random().nextLong();
        InventoryPojo inventory = insertHelper(globalSkuId,quantity);
        assertEquals(globalSkuId,inventory.getGlobalSkuId());
        assertEquals(quantity,inventory.getAvailableQuantity());
        assertEquals(Long.valueOf(0L),inventory.getAllocatedQuantity());
        assertEquals(Long.valueOf(0L),inventory.getFulfilledQuantity());
    }
    @Test
    public void addIntoExisting(){
        Long globalSkuId = new Random().nextLong();
        Long quantity1 = new Random().nextLong();
        insertHelper(globalSkuId,quantity1);
        Long quantity2 = new Random().nextLong();
        InventoryPojo inventory = insertHelper(globalSkuId,quantity2);
        assertEquals(globalSkuId,inventory.getGlobalSkuId());
        assertEquals(Long.valueOf(quantity1 + quantity2),inventory.getAvailableQuantity());
    }

    @Test
    public void updateStock() throws ApiException {
        Long globalSkuId = new Random().nextLong();
        Long alreadyQuantity = new Random().nextLong();
        insertHelper(globalSkuId,alreadyQuantity);
        Long newQuantity = new Random().nextLong();
        InventoryPojo updatePojo = new InventoryPojo().builder()
                .globalSkuId(globalSkuId)
                .availableQuantity(newQuantity)
                .build();
        InventoryPojo updatedInventory = service.updateAvailableStock(updatePojo);
        assertEquals(globalSkuId,updatedInventory.getGlobalSkuId());
        assertEquals(Long.valueOf(alreadyQuantity + newQuantity),updatedInventory.getAvailableQuantity());
    }

    @Test(expected = ApiException.class)
    public void invalidUpdateStock() throws ApiException {
        insertHelper(new Random().nextLong(),new Random().nextLong());
        Long newQuantity = new Random().nextLong();
        InventoryPojo updatePojo = new InventoryPojo().builder()
                .globalSkuId(new Random().nextLong())
                .availableQuantity(newQuantity)
                .build();
        try{
            InventoryPojo updatedInventory = service.updateAvailableStock(updatePojo);
        }
        catch (ApiException e){
            assertEquals("No such product exists in inventory.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void getAll() {
        for(int i =0 ;i<5;i ++){
            insertHelper(new Random().nextLong(),new Random().nextLong());
        }
        List<InventoryPojo> skuList = service.getAll();
        assertEquals(5,skuList.size());
    }

    @Test
    public void allocate() throws ApiException {
        Long globalSkuId = new Random().nextLong();
        Long availableQuantity = 12L;
        Long orderedQuantity = 23L;
        Long alreadyAllQuantity = 6L;
        InventoryPojo inventory = insertHelper(globalSkuId,availableQuantity);
        OrderItemPojo orderItem = new OrderItemPojo().builder()
                .globalSkuId(globalSkuId)
                .allocatedQuantity(alreadyAllQuantity)
                .orderedQuantity(orderedQuantity).build();
        Long allocatedQuantity = service.allocate(orderItem);
        Long expectedAllocation = Math.min(availableQuantity,orderedQuantity-alreadyAllQuantity);
        assertEquals(expectedAllocation,allocatedQuantity);
    }

    @Test
    public void fullfill() throws ApiException {
        Long globalSkuId = new Random().nextLong();
        Long orderedQuantity = 23L;
        Long alreadyAllQuantity = 23L;
        InventoryPojo inventoryPojo = new InventoryPojo().builder()
                .globalSkuId(globalSkuId)
                .allocatedQuantity(alreadyAllQuantity)
                .fulfilledQuantity(0L)
                .build();
        service.add(inventoryPojo);
        OrderItemPojo orderItem = new OrderItemPojo().builder()
                .globalSkuId(globalSkuId)
                .allocatedQuantity(alreadyAllQuantity)
                .orderedQuantity(orderedQuantity).build();
        InventoryPojo fullFilledInventory = service.fulFill(orderItem);
        assertEquals(orderedQuantity,fullFilledInventory.getFulfilledQuantity());
        assertEquals(Long.valueOf(alreadyAllQuantity-orderedQuantity),fullFilledInventory.getAllocatedQuantity());
    }
    public InventoryPojo insertHelper(Long globalSkuId, Long quantity){
        InventoryPojo inventory = new InventoryPojo().builder()
                .globalSkuId(globalSkuId)
                .allocatedQuantity(0L)
                .fulfilledQuantity(0L)
                .availableQuantity(quantity)
                .build();
        return service.add(inventory);
    }
}

package com.increff.dto.helper;

import com.increff.model.data.InventoryData;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;

public class InventoryDtoHelper {
    public static InventoryPojo convertToNewPojo(Long globalSkuId, Long quantity){
        InventoryPojo pojo = new InventoryPojo();
        pojo.setGlobalSkuId(globalSkuId);
        pojo.setAvailableQuantity(quantity);
        pojo.setAllocatedQuantity(0L);
        pojo.setFulfilledQuantity(0L);
        return pojo;
    }

    public static InventoryPojo updateStockPojo(BinSkuPojo existingBinSku, Long quantity) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setGlobalSkuId(existingBinSku.getGlobalSkuId());
        pojo.setAvailableQuantity(quantity-existingBinSku.getQuantity());
        return pojo;
    }

    public static InventoryData convertToData(InventoryPojo pojo) {
        InventoryData data = new InventoryData();
        data.setId(pojo.getId());
        data.setAllocatedQuantity(pojo.getAllocatedQuantity());
        data.setFulfilledQuantity(pojo.getFulfilledQuantity());
        data.setAvailableQuantity(pojo.getAvailableQuantity());
        return data;
    }
}

package com.increff.service;

import com.increff.dao.InventoryDao;
import com.increff.exception.ApiException;
import com.increff.pojo.InventoryPojo;
import com.increff.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo add(InventoryPojo inputInventory) {
        InventoryPojo existingInventory = inventoryDao.get(inputInventory.getGlobalSkuId());
        if (existingInventory != null) {
            existingInventory.setAvailableQuantity(existingInventory.getAvailableQuantity() + inputInventory.getAvailableQuantity());
            return existingInventory;
        }
        return inventoryDao.create(inputInventory);
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo updateAvailableStock(InventoryPojo inputInventory) throws ApiException {
        InventoryPojo existingInventory = getInventoryByGSkuId(inputInventory.getGlobalSkuId());
        existingInventory.setAvailableQuantity(existingInventory.getAvailableQuantity() + inputInventory.getAvailableQuantity());
        return existingInventory;
    }

    @Transactional(rollbackFor = ApiException.class)
    public Long allocate(OrderItemPojo item) throws ApiException {
        InventoryPojo inventory = getInventoryByGSkuId(item.getGlobalSkuId());
        Long availableToAllocateQuantity = Math.min(inventory.getAvailableQuantity(), item.getOrderedQuantity() - item.getAllocatedQuantity());
        inventory.setAllocatedQuantity(availableToAllocateQuantity + inventory.getAllocatedQuantity());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - availableToAllocateQuantity);
        return availableToAllocateQuantity;
    }

    @Transactional(readOnly = true)
    private InventoryPojo getInventoryByGSkuId(Long globalSkuId) throws ApiException {
        InventoryPojo inventory = inventoryDao.get(globalSkuId);
        if (inventory == null)
            throw new ApiException("No such product exists in inventory.");
        return inventory;
    }

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo fulFill(OrderItemPojo item) throws ApiException {
        Long orderedQuantity = item.getOrderedQuantity();
        InventoryPojo inventory = getInventoryByGSkuId(item.getGlobalSkuId());
        inventory.setFulfilledQuantity(inventory.getFulfilledQuantity() + orderedQuantity);
        inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() - orderedQuantity);
        return inventory;
    }

    @Transactional(readOnly = true)
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }
}

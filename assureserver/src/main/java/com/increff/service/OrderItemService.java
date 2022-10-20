package com.increff.service;

import com.increff.dao.OrderItemDao;
import com.increff.exception.ApiException;
import com.increff.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getByOrderId(Long orderId) {
        return  orderItemDao.getByOrderId(orderId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) {
        orderItemDao.add(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public OrderItemPojo getById(Long id) throws ApiException {
        OrderItemPojo orderItem = orderItemDao.getById(id);
        if(orderItem == null)
            throw new ApiException("No such item exists.");
        return orderItem;
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo allocate(OrderItemPojo inputOrder, Long allocatedQuantity) throws ApiException {
        OrderItemPojo orderItemPojo =  getById(inputOrder.getId());
        orderItemPojo.setAllocatedQuantity(allocatedQuantity + orderItemPojo.getAllocatedQuantity());
        return orderItemPojo;
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderItemPojo fulFill(OrderItemPojo orderItem) throws ApiException {
        OrderItemPojo item = getById(orderItem.getId());
        item.setFulfilledQuantity(item.getFulfilledQuantity() + item.getOrderedQuantity());
        item.setAllocatedQuantity(item.getAllocatedQuantity() - item.getOrderedQuantity());
        return item;
    }
}

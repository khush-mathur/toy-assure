package com.increff.service;

import com.increff.dao.OrderDao;
import com.increff.enums.OrderStatus;
import com.increff.exception.ApiException;
import com.increff.model.form.OrderCsvForm;
import com.increff.pojo.OrderItemPojo;
import com.increff.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo create(OrderPojo orderPojo) {
        return orderDao.create(orderPojo);
    }

    @Transactional(readOnly = true)
    public OrderPojo getByChannOrderId(Long channelId, String channelOrderId) {
        return  orderDao.getByChannOrderId(channelId,channelOrderId);
    }

    @Transactional(readOnly = true)
    public OrderPojo getById(Long orderId) throws ApiException {
        OrderPojo order = orderDao.getById(orderId);
        if(order == null)
            throw new ApiException("No order with id: " + orderId + " exists.");
        return order;
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getAll() {
        return  orderDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateStatus(Long orderId, OrderStatus status) {
        OrderPojo order = orderDao.getById(orderId);
        order.setStatus(status);
    }

}

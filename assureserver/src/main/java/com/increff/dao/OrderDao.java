package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.OrderItemPojo;
import com.increff.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {

    private final static String SELECT_BY_CHANID_ORDER_ID = "select u from assure_order u where channelId  = :channelId and channelOrderId = :channelOrderId";
    private final static String SELECT_ORDER_BY_ID = "select u from assure_order u where id  = :id";

    private final static String SELECT_ORDER_ITEM_BY_ID = "select u from assure_order_item u where id  = :id";
    private final static String SELECT_ALL = "select u from assure_order u";

    @Transactional(readOnly = true)
    public OrderPojo getByChannOrderId(Long channelId, String channelOrderId) {
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_CHANID_ORDER_ID, OrderPojo.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelOrderId", channelOrderId);
        return getSingleRow(query);
    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo create(OrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public OrderPojo getById(Long orderId) {
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_ORDER_BY_ID, OrderPojo.class);
        query.setParameter("id", orderId);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> selectAll() {
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_ALL,OrderPojo.class);
        return query.getResultList();
    }

//    public OrderItemPojo getItemById(Long id) {
//        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_ORDER_ITEM_BY_ID, OrderItemPojo.class);
//        query.setParameter("id", id);
//        return getSingleRow(query);
//    }
}

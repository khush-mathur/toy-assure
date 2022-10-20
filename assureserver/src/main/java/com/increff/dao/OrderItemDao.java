package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private final static String SELECT_ALL_BY_ORDER_ID = "select d from assure_order_item d where orderId = :orderId";
    private final static String SELECT_BY_ID = "select d from assure_order_item d where id = :id";

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getByOrderId(Long orderId){
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_ALL_BY_ORDER_ID,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }
    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemPojo orderItem) {
        em().persist(orderItem);
    }

    @Transactional(readOnly = true)
    public OrderItemPojo getById(Long id) {
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_BY_ID,OrderItemPojo.class);
        query.setParameter("id",id);
        return getSingleRow(query);
    }
}

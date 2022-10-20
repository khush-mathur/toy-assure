package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelOrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelOrderItemDao extends AbstractDao {
    private final static String SELECT_ALL_BY_ORDER_ID = "select d from channel_order_item d where orderId = :orderId";
    private final static String SELECT_BY_ID = "select d from channel_order_item d where id = :id";

    @Transactional(readOnly = true)
    public List<ChannelOrderItemPojo> getByOrderId(Long orderId){
        TypedQuery<ChannelOrderItemPojo> query = em().createQuery(SELECT_ALL_BY_ORDER_ID,ChannelOrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }
    @Transactional(rollbackFor = ApiException.class)
    public void add(ChannelOrderItemPojo orderItem) {
        em().persist(orderItem);
    }

    @Transactional(readOnly = true)
    public ChannelOrderItemPojo getById(Long id) {
        TypedQuery<ChannelOrderItemPojo> query = em().createQuery(SELECT_BY_ID,ChannelOrderItemPojo.class);
        query.setParameter("id",id);
        return getSingleRow(query);
    }
}

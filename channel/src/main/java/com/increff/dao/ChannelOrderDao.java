package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelOrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelOrderDao extends AbstractDao {
    private final static String SELECT_ALL = "select u from channel_order u";

    private final static String SELECT_ORDER_BY_ID = "select u from channel_order u where id  = :id";

    @Transactional(rollbackFor = ApiException.class)
    public ChannelOrderPojo create(ChannelOrderPojo orderPojo) {
        em().persist(orderPojo);
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public List<ChannelOrderPojo> selectAll() {
        TypedQuery<ChannelOrderPojo> query = em().createQuery(SELECT_ALL,ChannelOrderPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ChannelOrderPojo getById(Long orderId) {
        TypedQuery<ChannelOrderPojo> query = em().createQuery(SELECT_ORDER_BY_ID, ChannelOrderPojo.class);
        query.setParameter("id", orderId);
        return getSingleRow(query);
    }
}

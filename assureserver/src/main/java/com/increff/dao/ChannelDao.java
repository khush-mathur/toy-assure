package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao {
    private static final String SELECT_ALL = "select u from assure_channel u";
    private static final String SELECT_BY_NAME = "select u from assure_channel u where name  = :name";
    private static final String SELECT_BY_ID = "select u from assure_channel u where id  = :id";

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo create(ChannelPojo user) {
        em().persist(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<ChannelPojo> selectAll() {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_ALL, ChannelPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ChannelPojo getByName(String name) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_NAME, ChannelPojo.class);
        query.setParameter("name", name);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getById(Long channelId) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("id", channelId);
        return getSingleRow(query);
    }
}

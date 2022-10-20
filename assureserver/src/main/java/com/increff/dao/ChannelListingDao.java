package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao {
    private final static String SELECT_BY_CLTID_CHANID_CHANSKU = "select d from assure_channel_listing d where clientId = :clientId and " +
            "channelId = :channelId and channelSkuId = :channelSkuId";
    private final static String SELECT_ALL = "select d from assure_channel_listing d";
    private final static String SELECT_BY_ID = "select d from assure_channel_listing d where id = :id";

    @Transactional(readOnly = true)
    public List<ChannelListingPojo> getAll() {
        TypedQuery<ChannelListingPojo> query = em().createQuery(SELECT_ALL,ChannelListingPojo.class);
        return query.getResultList();
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelListingPojo add(ChannelListingPojo pojo) {
        em().persist(pojo);
        return pojo;
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo get(Long clientId, Long channelId, String channelSkuId) {
        TypedQuery<ChannelListingPojo> query = em().createQuery(SELECT_BY_CLTID_CHANID_CHANSKU,ChannelListingPojo.class);
        query.setParameter("clientId",clientId);
        query.setParameter("channelId",channelId);
        query.setParameter("channelSkuId",channelSkuId);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo get(Long id) {
        TypedQuery<ChannelListingPojo> query = em().createQuery(SELECT_BY_ID,ChannelListingPojo.class);
        query.setParameter("id",id);
        return getSingleRow(query);
    }
}

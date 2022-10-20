package com.increff.service;

import com.increff.dao.ChannelListingDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelListingService {

    @Autowired
    private ChannelListingDao channelListingDao;

    @Transactional(readOnly = true)
    public List<ChannelListingPojo> getAll() {
        return  channelListingDao.getAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelListingPojo create(ChannelListingPojo pojo) throws ApiException {
        if (listingAlreadyExists(pojo)) {
            throw new ApiException("Listing with channel Sku Id "+ pojo.getChannelSkuId()  + " already exists of channel " + pojo.getChannelId());
        }
        return channelListingDao.add(pojo);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo getById(Long id) throws ApiException {
        ChannelListingPojo channelListing = channelListingDao.get(id);
        if(channelListing == null)
            throw new ApiException("No such listing exists.");
        return channelListing;
    }
    @Transactional(readOnly = true)
    public ChannelListingPojo getByChanClientAndSkuId(Long channId, Long clientId, String channSkuId) throws ApiException {
        ChannelListingPojo channelListing = channelListingDao.get(clientId,channId,channSkuId);
        if(channelListing == null)
            throw new ApiException("No such listing exists.");
        return channelListing;
    }
    @Transactional(readOnly = true)
    private boolean listingAlreadyExists(ChannelListingPojo inputPojo){
        return channelListingDao.get(inputPojo.getClientId(), inputPojo.getChannelId(), inputPojo.getChannelSkuId()) != null;
    }
}

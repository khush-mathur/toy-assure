package com.increff.service;

import com.increff.dao.ChannelDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Transactional(readOnly = true)
    public ChannelPojo getByName(String name) throws ApiException {
        return  channelDao.getByName(name);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ChannelPojo create(ChannelPojo channel) {
        return channelDao.create(channel);
    }

    @Transactional(readOnly = true)
    public List<ChannelPojo> getAll() {
        return channelDao.selectAll();
    }

    @Transactional(readOnly = true)
    public ChannelPojo getById(Long channelId) throws ApiException {
        ChannelPojo channel = channelDao.getById(channelId);
        if(channel == null)
            throw new ApiException("No such channel exists.");
        return channel;
    }
}

package com.increff.controller;

import com.increff.dto.ChannelListingDto;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.form.ChannelForm;
import com.increff.model.form.ChannelListingForm;
import com.increff.model.retrundata.ChannelListingReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/channel-listing")
public class ChannelListingController {
    @Autowired
    private ChannelListingDto channelListingDto;

    @ApiOperation(value = "Adds Channel Listing")
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public List<ChannelListingReturnData> add(@RequestParam String clientName, @RequestParam String channelName, @RequestBody List<ChannelListingForm> form) throws ApiException {
        return channelListingDto.create(clientName,channelName,form);
    }

    @ApiOperation(value = "Gets All the listings")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<ChannelListingData> getAll() throws ApiException {
        return channelListingDto.fetchAll();
    }


    @ApiOperation(value = "Get a listing by Id")
    @RequestMapping(path = "/view/{id}",method = RequestMethod.GET)
    public ChannelListingData get(@PathVariable Long id) throws ApiException {
        return channelListingDto.getById(id);
    }
}

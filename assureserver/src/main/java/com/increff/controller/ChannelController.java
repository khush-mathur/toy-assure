package com.increff.controller;

import com.increff.dto.ChannelDto;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.data.UserData;
import com.increff.model.form.ChannelForm;
import com.increff.model.form.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/channel")
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;

    @ApiOperation(value = "Creates a new Channel")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ChannelData add(@RequestBody ChannelForm form) throws ApiException {
        return channelDto.create(form);
    }

    @ApiOperation(value = "Gets list of all Channels")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<ChannelData> getAll() {
        return channelDto.fetchAll();
    }

//    @ApiOperation(value = "Update a channel by id")
//    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
//    public ChannelData update(@PathVariable Long id, @RequestBody ChannelForm channelForm) throws ApiException {
//        return channelDto.update(id, channelForm);
//    }

    @ApiOperation(value = "Get a user by Id")
    @RequestMapping(path = "/view/{id}",method = RequestMethod.GET)
    public ChannelData get(@PathVariable Long id) throws ApiException {
        return channelDto.getById(id);
    }
}

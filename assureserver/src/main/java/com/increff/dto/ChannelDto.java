package com.increff.dto;

import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.enums.InvoiceType;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.form.ChannelForm;
import com.increff.pojo.ChannelPojo;
import com.increff.service.ChannelService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelDto {
    @Autowired
    private ChannelService channelService;

    public ChannelData create(ChannelForm rawForm) throws ApiException {
        ChannelForm form = normalise(rawForm);
        if(!validate(form) || channelService.getByName(form.getName())!=null) {    // instead check for each name by fetching all coz of capital name
            throw new ApiException("Channel already exists.");
        }
        ChannelPojo pojo = channelService.create(ChannelDtoHelper.convertToPojo(form));
        return ChannelDtoHelper.convertToData(pojo);
    }

    public List<ChannelData> fetchAll() {
        List<ChannelData> channelData = new ArrayList<>();
        for (ChannelPojo pojo : channelService.getAll()) {
            channelData.add(ChannelDtoHelper.convertToData(pojo));
        }
        return channelData;
    }

    public ChannelData getById(Long id) throws ApiException {
        ChannelPojo pojo = channelService.getById(id);
        return ChannelDtoHelper.convertToData(pojo);
    }

    private boolean validate(ChannelForm form) throws ApiException {
        if(form.getName()==null || form.getName().isEmpty())
            throw new ApiException("Invalid Input : name cannot be empty");
        else if(form.getInvoiceType()==null ||form.getInvoiceType().isEmpty() || !EnumUtils.isValidEnum(InvoiceType.class, form.getInvoiceType()))
            throw new ApiException("Invalid input : Enter a valid Invoice Type");
        return true;
    }
    private ChannelForm normalise(ChannelForm channel){
        channel.setName(channel.getName().trim().toLowerCase());
        return channel;
    }
}

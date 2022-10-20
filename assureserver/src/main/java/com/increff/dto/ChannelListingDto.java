package com.increff.dto;

import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.dto.helper.ChannelListingDtoHelper;
import com.increff.dto.helper.ProductDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.form.ChannelForm;
import com.increff.model.form.ChannelListingForm;
import com.increff.model.retrundata.ChannelListingReturnData;
import com.increff.model.retrundata.ProductReturnData;
import com.increff.pojo.ChannelListingPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.ProductPojo;
import com.increff.service.ChannelListingService;
import com.increff.service.ChannelService;
import com.increff.service.ProductService;
import com.increff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelListingDto {
    @Autowired
    private ChannelListingService channelListingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ProductService productService;

    public List<ChannelListingData> fetchAll() throws ApiException {
        List<ChannelListingData> listingDataList = new ArrayList<>();
        for (ChannelListingPojo listingPojo : channelListingService.getAll()){
            ChannelListingData channelListing =  ChannelListingDtoHelper.convertToData(listingPojo);
            channelListing.setChannelName(channelService.getById(listingPojo.getChannelId()).getName());
            channelListing.setClientName(userService.getById(listingPojo.getClientId()).getName());
            channelListing.setClientSkuId(productService.getProductByGSkuId(listingPojo.getGlobalSkuId()).getClientSkuId());
            listingDataList.add(channelListing);
        }
        return listingDataList;
    }

    public List<ChannelListingReturnData> create(String clientName, String channelName, List<ChannelListingForm> formList) throws ApiException{
        List<ChannelListingReturnData> message = new ArrayList<>();
        if(!validateInput(clientName,channelName))
            throw new ApiException("Invalid input");
        for(ChannelListingForm rawChannelListing:formList) {
            ChannelListingForm channelListing = normalise(rawChannelListing);
            boolean uploadStatus = true;
            try {
                validateFormInput(channelListing);
                ProductPojo product = productService.getProductByClientIdNSkuId(userService.getByName(clientName).getId(),
                        channelListing.getClientSkuId());
                if (product==null)
                    throw new ApiException("No such product exists.");
                ChannelListingPojo pojo = ChannelListingDtoHelper.convertToPojo(channelListing);
                pojo.setChannelId(channelService.getByName(channelName.trim().toLowerCase()).getId());
                pojo.setClientId(userService.getByName(clientName.trim().toLowerCase()).getId());
                pojo.setGlobalSkuId(productService.getProductByClientIdNSkuId(pojo.getClientId(),
                        channelListing.getClientSkuId()).getGlobalSkuId());
                channelListingService.create(pojo);
            }
            catch (ApiException e){
                uploadStatus = false;
                message.add( new ChannelListingReturnData().builder()
                                .channelName(channelName).clientName(clientName)
                                .channelSku(channelListing.getChannelSkuId().trim().toLowerCase())
                                .log("Unable to add Listing - " + e.getMessage()).build() );
            }
            if(uploadStatus){
                message.add( new ChannelListingReturnData().builder()
                        .channelName(channelName).clientName(clientName)
                        .channelSku(channelListing.getChannelSkuId().trim().toLowerCase())
                        .log("Channel Listing Added").build() );
            }
        }
        return message;
    }

    public ChannelListingData getById(Long id) throws ApiException {
        ChannelListingPojo pojo = channelListingService.getById(id);
        if(pojo==null)
            throw new ApiException("No such channel exist.");
        ChannelListingData listingData =  ChannelListingDtoHelper.convertToData(pojo);
        listingData.setClientName(userService.getById(pojo.getClientId()).getName());
        listingData.setChannelName(channelService.getById(pojo.getChannelId()).getName());
        listingData.setClientSkuId(productService.getProductByGSkuId(pojo.getGlobalSkuId()).getClientSkuId());
        return listingData;
    }
    private boolean validateInput(String clientName, String channelName) throws ApiException {
        if(clientName==null || clientName.isEmpty())
            throw new ApiException("Invalid Input : Client Name cannot be empty");
        if(userService.getByName(clientName)==null)
            throw new ApiException("Invalid Input : No such client exist");
        if(channelName==null || channelName.isEmpty())
            throw new ApiException("Invalid Input : Channel Name cannot be empty");
        if(channelService.getByName(channelName)==null)
            throw new ApiException("Invalid Input : No such channel exist");
        return true;
    }

    private boolean validateFormInput(ChannelListingForm listingForm) throws ApiException {
        if(listingForm.getChannelSkuId()==null || listingForm.getChannelSkuId().isEmpty())
            throw new ApiException("Invalid Input : Enter correct ChannelSku- ID");
        else if(listingForm.getClientSkuId()==null || listingForm.getClientSkuId().isEmpty())
            throw new ApiException("Invalid Input : Enter correct ClientSku-ID");
        return true;
    }
    private ChannelListingForm normalise(ChannelListingForm form){
        form.setChannelSkuId(form.getChannelSkuId().trim().toLowerCase());
        form.setClientSkuId(form.getClientSkuId().trim().toLowerCase());
        return form;
    }
}

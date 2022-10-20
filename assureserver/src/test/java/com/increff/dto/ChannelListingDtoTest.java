package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.model.data.ChannelData;
import com.increff.model.data.ChannelListingData;
import com.increff.model.data.UserData;
import com.increff.model.form.ChannelForm;
import com.increff.model.form.ChannelListingForm;
import com.increff.model.form.ProductForm;
import com.increff.model.form.UserForm;
import com.increff.model.retrundata.ChannelListingReturnData;
import com.increff.model.retrundata.ProductReturnData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChannelListingDtoTest extends AbstractUnitTest {
    @Resource
    ChannelListingDto channelListingDto;
    @Resource
    ChannelDto channelDto;
    @Resource
    UserDto userDto;

    @Resource
    ProductDto productDto;

    @Test
    public void createListing() throws ApiException {
        UserData client = createClient();
        ProductReturnData product = createProduct(client.getId());
        String clientSku = product.getClientSkuId();
        String channelSku = RandomStringUtils.random(6,true,true);
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(channelSku,clientSku));
        List<ChannelListingReturnData> returnData =channelListingDto.create(client.getName(),createChannel().getName(),formList);
        List<ChannelListingData> listingData = channelListingDto.fetchAll();
        assertEquals("Channel Listing Added",returnData.get(0).getLog());
        assertEquals(clientSku.trim().toLowerCase(),listingData.get(0).getClientSkuId());
        assertEquals(channelSku.trim().toLowerCase(),listingData.get(0).getChannelSkuId());
    }

    @Test(expected = ApiException.class)
    public void createProductWBlankClient() throws ApiException {
        String clientSku = RandomStringUtils.random(6,true,true);
        String channelSku = RandomStringUtils.random(6,true,true);
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(channelSku,clientSku));
        try {
            channelListingDto.create("", createChannel().getName(), formList);
        }
        catch (ApiException e) {
            assertEquals("Invalid Input : Client Name cannot be empty", e.getMessage());
            throw e;
        }
    }
    @Test(expected = ApiException.class)
    public void createProductWBlankChannel() throws ApiException {
        String clientSku = RandomStringUtils.random(6,true,true);
        String channelSku = RandomStringUtils.random(6,true,true);
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(channelSku,clientSku));
        try {
            channelListingDto.create(createClient().getName(), "", formList);
        }
        catch(ApiException e) {
            assertEquals("Invalid Input : Channel Name cannot be empty", e.getMessage());
            throw e;
        }
    }
    @Test(expected = ApiException.class)
    public void createProductWInvalidClient() throws ApiException {
        String clientSku = RandomStringUtils.random(6,true,true);
        String channelSku = RandomStringUtils.random(6,true,true);
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(channelSku,clientSku));
        try{
            channelListingDto.create(RandomStringUtils.random(6),createChannel().getName(),formList);
        }
        catch (ApiException e) {
            assertEquals("Invalid Input : No such client exist", e.getMessage());
            throw e;
        }
    }
    @Test(expected = ApiException.class)
    public void createProductWInvalidChannel() throws ApiException {
        String clientSku = RandomStringUtils.random(6,true,true);
        String channelSku = RandomStringUtils.random(6,true,true);
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(channelSku,clientSku));
        try{
            channelListingDto.create(createClient().getName(),RandomStringUtils.random(6),formList);
        }
        catch (ApiException e){
            assertEquals("Invalid Input : No such channel exist",e.getMessage());
            throw e;
        }
    }

    @Test
    public void createProductWInvalidInput() throws ApiException {
        UserData client = createClient();
        ProductReturnData product = createProduct(client.getId());
        String clientSku = product.getClientSkuId();
        String channelSku = RandomStringUtils.random(6,true,true);

        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm("",clientSku));
        formList.add(createChannelListingForm(channelSku,""));
        formList.add(createChannelListingForm(channelSku,RandomStringUtils.random(5)));

        List<ChannelListingReturnData> listingReturnData = channelListingDto.create(client.getName(),createChannel().getName(), formList);
        assertEquals("Unable to add Listing - Invalid Input : Enter correct ChannelSku- ID",listingReturnData.get(0).getLog());
        assertEquals("Unable to add Listing - Invalid Input : Enter correct ClientSku-ID",listingReturnData.get(1).getLog());
        assertEquals("Unable to add Listing - No such product exists.",listingReturnData.get(2).getLog());

    }

    @Test
    public void getAll() throws ApiException {
        List<ChannelListingForm> formList = new ArrayList<>();
        UserData client = createClient();
        for(int i =0;i<3;i++){
            String clientSku = createProduct(client.getId()).getClientSkuId();
            formList.add(createChannelListingForm(RandomStringUtils.random(6),clientSku));
        }
        channelListingDto.create(client.getName(),createChannel().getName(),formList);
        List<ChannelListingData> listingDataList = channelListingDto.fetchAll();
        assertEquals(3,listingDataList.size());
    }

    @Test
    public void getById() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(RandomStringUtils.random(6),clientSku));
        List<ChannelListingReturnData> listingReturnData = channelListingDto.create(client.getName(),createChannel().getName(),formList);
        List<ChannelListingData> listingDataList = channelListingDto.fetchAll();
        ChannelListingData fetchedListing = channelListingDto.getById(listingDataList.get(0).getId());
        assertEquals(listingReturnData.get(0).getChannelName(),fetchedListing.getChannelName());
        assertEquals(listingReturnData.get(0).getClientName(),fetchedListing.getClientName());
    }

    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        List<ChannelListingForm> formList = new ArrayList<>();
        formList.add(createChannelListingForm(RandomStringUtils.random(6),RandomStringUtils.random(6)));
        channelListingDto.create(createClient().getName(),createChannel().getName(),formList);
        try {
            channelListingDto.getById(new Random().nextLong());
        }
        catch (ApiException e){
            assertEquals("No such listing exists.",e.getMessage());
            throw e;
        }
    }

    private UserData createClient() throws ApiException {
        return userDto.create ( new UserForm().builder().name(RandomStringUtils.random(6)).type("CLIENT").build());
    }

    private ChannelData createChannel() throws ApiException {
        return channelDto.create ( new ChannelForm().builder().name(RandomStringUtils.random(6)).invoiceType("SELF").build());
    }

    private ChannelListingForm createChannelListingForm(String channelSku , String clientSku){
        return new ChannelListingForm().builder().channelSkuId(channelSku).clientSkuId(clientSku).build();
    }

    private ProductReturnData createProduct(Long clientId){
        List<ProductForm> list = new ArrayList<>();
        list.add(new ProductForm().builder()
                .name(RandomStringUtils.random(6)).brand(RandomStringUtils.random(6))
                .mrp(String.valueOf(new Random().nextDouble()))
                .description(RandomStringUtils.random(6)).clientSkuId(RandomStringUtils.random(6))
                .build());
        return productDto.create(clientId,list).get(0);
    }
}

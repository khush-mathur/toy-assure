package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.model.data.BinData;
import com.increff.model.data.BinSkuData;
import com.increff.model.data.InventoryData;
import com.increff.model.data.UserData;
import com.increff.model.form.BinSkuForm;
import com.increff.model.form.ProductForm;
import com.increff.model.form.UserForm;
import com.increff.model.retrundata.ProductReturnData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BinSkuDtoTest extends AbstractUnitTest {
    @Resource
    BinSkuDto binSkuDto;
    @Resource
    ProductDto productDto;
    @Resource
    UserDto userDto;
    @Resource
    BinDto binDto;

    @Test
    public void binSkuAdd() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        binSkuDto.add(client.getId(),formList);
        List<BinSkuData> skuData = binSkuDto.fetchAllBinWise();
        assertEquals(clientSku.trim().toLowerCase(),skuData.get(0).getClientSkuId());
        assertEquals(binId,skuData.get(0).getBinId());
        assertEquals(quantity,skuData.get(0).getQuantity());
    }

    @Test(expected = ApiException.class)
    public void binSkuAddInvalidClient() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        try{
            binSkuDto.add(new Random().nextLong(),formList);
        }
        catch(ApiException e){
            assertEquals("No such client exists",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void binSkuAddDuplicateSku() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        formList.add(createBinSkuForm(binId,clientSku,Math.abs(new Random().nextLong())));
        try{
            binSkuDto.add(client.getId(), formList);
        }
        catch(ApiException e){
            assertEquals("Invalid Input : Input has duplicate binid-skuId",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void binSkuAddInvalidSku() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,RandomStringUtils.random(5),quantity));
        try{
            binSkuDto.add(client.getId(), formList);
        }
        catch(ApiException e){
            assertEquals("No such product exists.",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void binSkuAddInvalidBinId() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = new Random().nextLong();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        try{
            binSkuDto.add(client.getId(), formList);
        }
        catch(ApiException e){
            assertEquals("No bin with id: " + binId + " exists.",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void binSkuAddInvalidQuantity() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,-5L));
        try{
            binSkuDto.add(client.getId(), formList);
        }
        catch(ApiException e){
            assertEquals("Invalid Input : Enter correct Quantity",e.getMessage());
            throw e;
        }
    }
    @Test(expected = ApiException.class)
    public void binSkuAddBlankSku() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,"",quantity));
        try{
            binSkuDto.add(client.getId(), formList);
        }
        catch(ApiException e){
            assertEquals("Invalid Input : Enter correct SKU-ID",e.getMessage());
            throw e;
        }
    }


    @Test
    public void getAllBinWise() throws ApiException {
        List<BinSkuForm> formList = new ArrayList<>();
        UserData client = createClient();
        String clientSku1 = createProduct(client.getId()).getClientSkuId();
        for(int i =0;i<3;i++){
            formList.add(createBinSkuForm(createBin().getBinId(), clientSku1, Math.abs(new Random().nextLong())));
        }
        String clientSku2 = createProduct(client.getId()).getClientSkuId();
        for(int i =0;i<4;i++){
            formList.add(createBinSkuForm(createBin().getBinId(), clientSku2, Math.abs(new Random().nextLong())));
        }
        binSkuDto.add(client.getId(),formList);
        List<BinSkuData> binSkuList = binSkuDto.fetchAllBinWise();
        List<InventoryData> inventoryList = binSkuDto.fetchAll();
        assertEquals(2,inventoryList.size());
        assertEquals(7,binSkuList.size());
    }

    @Test
    public void getById() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        binSkuDto.add(client.getId(),formList);
        BinSkuData fetchedBinSku = binSkuDto.getById(binSkuDto.fetchAllBinWise().get(0).getId());
        assertEquals(clientSku.trim().toLowerCase(),fetchedBinSku.getClientSkuId());
        assertEquals(binId,fetchedBinSku.getBinId());
        assertEquals(quantity,fetchedBinSku.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity));
        binSkuDto.add(client.getId(),formList);
        try {
            binSkuDto.getById(new Random().nextLong());
        }
        catch (ApiException e){
            assertEquals("No such bin-sku exists.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void binSkuUpdate() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity1 = Math.abs(new Random().nextLong());
        Long quantity2 = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity1));
        formList.add(createBinSkuForm(createBin().getBinId(), clientSku,quantity2));
        binSkuDto.add(client.getId(),formList);
        Long newQuantity = Math.abs(new Random().nextLong());
        BinSkuData updatedBinSku =  binSkuDto.update(binSkuDto.fetchAllBinWise().get(0).getId(),createBinSkuForm(binId,clientSku,newQuantity));
        InventoryData updatedInventory = binSkuDto.fetchAll().get(0);
        assertEquals(binId,updatedBinSku.getBinId());
        assertEquals(clientSku.trim().toLowerCase(),updatedBinSku.getClientSkuId());
        assertEquals(newQuantity,updatedBinSku.getQuantity());
        assertEquals(clientSku.trim().toLowerCase(),updatedInventory.getClientSkuId());
        assertEquals(Long.valueOf(quantity2+newQuantity),updatedInventory.getAvailableQuantity());
    }

    @Test(expected = ApiException.class)
    public void binSkuInvalidUpdate() throws ApiException {
        UserData client = createClient();
        String clientSku = createProduct(client.getId()).getClientSkuId();
        Long binId = createBin().getBinId();
        Long quantity1 = Math.abs(new Random().nextLong());
        Long quantity2 = Math.abs(new Random().nextLong());
        List<BinSkuForm> formList = new ArrayList<>();
        formList.add(createBinSkuForm(binId,clientSku,quantity1));
        formList.add(createBinSkuForm(createBin().getBinId(), clientSku,quantity2));
        binSkuDto.add(client.getId(),formList);
        Long newQuantity = Math.abs(new Random().nextLong());
        try {
            binSkuDto.update(new Random().nextLong(), createBinSkuForm(binId, clientSku, newQuantity));
        }
        catch (ApiException e){
            assertEquals("No such bin-sku exists.",e.getMessage());
            throw e;
        }
    }

    private UserData createClient() throws ApiException {
        return userDto.create ( new UserForm().builder().name(RandomStringUtils.random(6)).type("CLIENT").build());
    }
    private BinData createBin() throws ApiException {
        return binDto.create (1L).get(0);
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
    private BinSkuForm createBinSkuForm(Long binId , String clientSku,Long quantity){
        return new BinSkuForm().builder().binId(binId).clientSkuId(clientSku).quantity(quantity)
                .build();
    }
}

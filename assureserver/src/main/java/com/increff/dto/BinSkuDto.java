package com.increff.dto;

import com.increff.dto.helper.BinSkuDtoHelper;
import com.increff.dto.helper.ChannelDtoHelper;
import com.increff.dto.helper.InventoryDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.BinSkuData;
import com.increff.model.data.InventoryData;
import com.increff.model.form.BinSkuForm;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.ChannelPojo;
import com.increff.pojo.InventoryPojo;
import com.increff.pojo.ProductPojo;
import com.increff.service.*;
import oracle.jrockit.jfr.events.Bits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinSkuDto {
    @Autowired
    BinSkuService binSkuService;
    @Autowired 
    BinService binService;
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    UserService userService;

    @Transactional(rollbackFor = ApiException.class)
    public void add(Long clientId,List<BinSkuForm> binSkuList) throws ApiException{
        if(!userService.isClient(clientId))
            throw new ApiException("No such client exists");
        if(!validateForm(binSkuList)) {
            throw new ApiException("Invalid Input : Input has duplicate binid-skuId");
        }
        for (BinSkuForm rawBinSku : binSkuList) {
            BinSkuForm binSku = normalise(rawBinSku);
            validateInput(binSku);
            ProductPojo product = productService.getProductByClientIdNSkuId(clientId, binSku.getClientSkuId());
            BinSkuPojo pojo = BinSkuDtoHelper.convertToPojo(binSku);
            pojo.setGlobalSkuId(product.getGlobalSkuId());
            binSkuService.add(pojo);
            inventoryService.add(InventoryDtoHelper.convertToNewPojo(pojo.getGlobalSkuId(),pojo.getQuantity()));
        }
    }



    public List<BinSkuData> fetchAllBinWise() throws ApiException {
        List<BinSkuData> binSkuData = new ArrayList<>();
        for (BinSkuPojo pojo : binSkuService.getAllBinWise()) {
            BinSkuData binData = BinSkuDtoHelper.convertToData(pojo);
            ProductPojo product = productService.getProductByGSkuId(pojo.getGlobalSkuId());
            binData.setProductName(product.getName());
            binData.setClientSkuId(product.getClientSkuId());
            binData.setClientName(userService.getById(product.getClientId()).getName());
            binSkuData.add(binData);
        }
        return binSkuData;
    }



    public BinSkuData update(Long id, BinSkuForm rawForm) throws ApiException {
        BinSkuForm form = normalise(rawForm);
        validateInput(form);
        BinSkuPojo existingBinSku = binSkuService.getById(id);
        inventoryService.updateAvailableStock(InventoryDtoHelper.updateStockPojo(existingBinSku,form.getQuantity()));
        BinSkuPojo binSkuPojo = binSkuService.update(id, BinSkuDtoHelper.convertToPojo(form));
        BinSkuData binData =  BinSkuDtoHelper.convertToData(binSkuPojo);
        ProductPojo product = productService.getProductByGSkuId(binSkuPojo.getGlobalSkuId());
        binData.setProductName(product.getName());
        binData.setClientSkuId(product.getClientSkuId());
        return binData;
    }

    public List<InventoryData> fetchAll() throws ApiException {
        List<InventoryData> inventoryList = new ArrayList<>();
        for (InventoryPojo pojo : inventoryService.getAll()) {
            InventoryData inventoryData = InventoryDtoHelper.convertToData(pojo);
            ProductPojo product = productService.getProductByGSkuId(pojo.getGlobalSkuId());
            inventoryData.setProductName(product.getName());
            inventoryData.setClientName(userService.getById(product.getClientId()).getName());
            inventoryData.setClientSkuId(product.getClientSkuId());
            inventoryList.add(inventoryData);
        }
        return inventoryList;
    }

    public BinSkuData getById(Long id) throws ApiException {
        BinSkuPojo binSkuPojo = binSkuService.getById(id);
        BinSkuData binSkuData =  BinSkuDtoHelper.convertToData(binSkuPojo);
        ProductPojo product = productService.getProductByGSkuId(binSkuPojo.getGlobalSkuId());
        binSkuData.setClientName(userService.getById(product.getClientId()).getName());
        binSkuData.setClientSkuId(product.getClientSkuId());
        binSkuData.setProductName(product.getName());
        return binSkuData;
    }

    private boolean validateInput(BinSkuForm binSku) throws ApiException {
        if(binSku.getBinId()==null || binService.getByBinId(binSku.getBinId())==null)
            throw new ApiException("Invalid Input : Enter correct Bin Id");
        if(binSku.getClientSkuId()==null || binSku.getClientSkuId().isEmpty())
            throw new ApiException("Invalid Input : Enter correct SKU-ID");
        if(binSku.getQuantity()==null || binSku.getQuantity() < 0)
            throw new ApiException("Invalid Input : Enter correct Quantity");
        return true;
    }
    private BinSkuForm normalise(BinSkuForm binSkuForm){
        binSkuForm.setClientSkuId(binSkuForm.getClientSkuId().trim().toLowerCase());
        return binSkuForm;
    }
    private boolean validateForm(List<BinSkuForm> formList){
        List<String> skuList = new ArrayList<>();
        for(BinSkuForm binSku : formList){
            if(skuList.contains(binSku.getBinId() + binSku.getClientSkuId()))
                return false;
            skuList.add(binSku.getBinId() + binSku.getClientSkuId());
        }
        return true;
    }
}

package com.increff.dto;

import com.google.common.primitives.Doubles;
import com.increff.dto.helper.ProductDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.ProductData;
import com.increff.model.form.ProductForm;
import com.increff.model.retrundata.ProductReturnData;
import com.increff.pojo.ProductPojo;
import com.increff.service.ProductService;
import com.increff.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    public List<ProductReturnData> create(Long clientId, List<ProductForm> productList) {
        List<ProductReturnData> message = new ArrayList<>();
        for(ProductForm rawProduct:productList) {
            ProductForm product = normalise(rawProduct);
            boolean uploadStatus = true;
            try {
                if(clientId==null)
                    throw new ApiException("Invalid Input : Client Id cannot be empty");
                if (!userService.isClient(clientId) || !validateInput(rawProduct))
                    throw new ApiException("No such client exists.");
                ProductPojo pojo = ProductDtoHelper.convertToProductPojo(product);
                pojo.setClientId(clientId);
                productService.create(pojo);
            }
            catch (ApiException e){
                uploadStatus = false;
                ProductReturnData returnData = ProductDtoHelper.convertToReturnData(product);
                returnData.setLog("Unable to add product " + e.getMessage());
                message.add(returnData);
            }
            if(uploadStatus){
                ProductReturnData returnData = ProductDtoHelper.convertToReturnData(product);
                returnData.setLog("Product Added");
                message.add(returnData);
            }
        }
        return message;
    }
    public ProductData get(Long gSkuId) throws ApiException{
        ProductPojo pojo = productService.getProductByGSkuId(gSkuId);
        ProductData product = ProductDtoHelper.convertToData(pojo);
        product.setClientName(userService.getById(pojo.getClientId()).getName());
        return product;
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductData> productList = new ArrayList<>();
        for (ProductPojo productPojo : productService.getAll()){
            ProductData product = ProductDtoHelper.convertToData(productPojo);
            product.setClientName(userService.getById(productPojo.getClientId()).getName());
            productList.add(product);
        }
        return productList;
    }

    public ProductData update(String clientName, ProductForm rawProductForm) throws ApiException{
        ProductForm productForm = normalise(rawProductForm);
        Long clientId = userService.getByName(clientName).getId();
        if (!validateInput(productForm) || productService.getProductByClientIdNSkuId(clientId, productForm.getClientSkuId())==null)
            throw new ApiException("No such product exists.");
        ProductPojo productPojo = ProductDtoHelper.convertToProductPojo(productForm);
        ProductData product = ProductDtoHelper.convertToData(productService.update(clientId,productPojo));
        product.setClientName(clientName);
        return product;
    }

    private ProductForm normalise(ProductForm product){
        product.setClientSkuId(product.getClientSkuId().trim().toLowerCase());
        product.setName(product.getName().trim().toLowerCase());
        product.setBrand(product.getBrand().trim().toLowerCase());
        product.setMrp(String.valueOf(Math.round(Double.parseDouble(product.getMrp()) * 100.0) / 100.0));
        product.setDescription(product.getDescription().trim().toLowerCase());
        return product;
    }

    private boolean validateInput(ProductForm product) throws ApiException {
        if(product.getBrand()==null || product.getBrand().isEmpty())
            throw new ApiException("Invalid Input : Enter correct Brand");
        else if(product.getMrp()==null || product.getMrp().isEmpty()
                || Doubles.tryParse(product.getMrp())==null || Double.valueOf(product.getMrp()) <=0)
            throw new ApiException("Invalid Input : Enter correct Mrp");
        if(product.getName()==null || product.getName().isEmpty())
            throw new ApiException("Invalid Input : Enter correct Name");
        else if(product.getDescription()==null || product.getDescription().isEmpty())
            throw new ApiException("Invalid Input : Enter correct Description");
        else if(product.getClientSkuId()==null || product.getClientSkuId().isEmpty())
            throw new ApiException("Invalid Input : Enter correct SKU Name");
        return true;
    }
}

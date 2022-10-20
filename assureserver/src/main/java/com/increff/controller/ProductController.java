package com.increff.controller;

import com.increff.dto.ProductDto;
import com.increff.exception.ApiException;
import com.increff.model.data.ProductData;
import com.increff.model.form.ProductForm;
import com.increff.model.retrundata.ProductReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping(path = "/product")
public class ProductController {
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds Product")
    @RequestMapping(path = "/upload/{clientId}", method = RequestMethod.POST)
    public List<ProductReturnData> add(@PathVariable Long clientId, @RequestBody List<ProductForm> form) throws ApiException {
        return productDto.create(clientId,form);
    }

    @ApiOperation(value = "Fetch Product")
    @RequestMapping(path = "/view/{globalSkuId}", method = RequestMethod.GET)
    public ProductData get(@PathVariable Long globalSkuId) throws ApiException {
        return productDto.get(globalSkuId);
    }

    @ApiOperation(value ="Fetches all the products")
    @RequestMapping(path = "/viewAll",method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException{
        return productDto.getAll();
    }

    @ApiOperation(value= "Updates a Existing Product")
    @RequestMapping(path="/update/{clientName}",method = RequestMethod.PUT)
    public ProductData update(@PathVariable String clientName, @RequestBody ProductForm form) throws ApiException {
        return productDto.update(clientName,form);
    }

}

package com.increff.controller;

import com.increff.dto.BinSkuDto;
import com.increff.exception.ApiException;
import com.increff.model.data.BinSkuData;
import com.increff.model.data.InventoryData;
import com.increff.model.form.BinSkuForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/inventory")
public class BinSkuController {
    @Autowired
    private BinSkuDto binSkuDto;

    @ApiOperation(value = "Adds Bin Wise Inventory")
    @RequestMapping(path = "/bin-wise/upload/{clientId}", method = RequestMethod.POST)
    public void upload(@PathVariable Long clientId , @RequestBody List<BinSkuForm> formList) throws ApiException {
        binSkuDto.add(clientId,formList);
    }

    @ApiOperation(value = "Gets list of all inventory bin wise.")
    @RequestMapping(path = "/bin-wise/viewAll", method = RequestMethod.GET)
    public List<BinSkuData> getAllBinWise() throws ApiException {
        return binSkuDto.fetchAllBinWise();
    }

    @ApiOperation(value = "Get a Bin Inventory by id")
    @RequestMapping(value = "/bin-wise/get/{id}", method = RequestMethod.GET)
    public BinSkuData get(@PathVariable Long id) throws ApiException {
        return binSkuDto.getById(id);
    }

    /***
     *
     * TODO: binId as input?
     */
    @ApiOperation(value = "Update a Bin Inventory by id")
    @RequestMapping(value = "/bin-wise/update/{id}", method = RequestMethod.PUT)
    public BinSkuData update(@PathVariable Long id, @RequestBody BinSkuForm form) throws ApiException {
        return binSkuDto.update(id, form);
    }

    @ApiOperation(value = "Gets list of all inventory.")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<InventoryData> getAll() throws ApiException {
        return binSkuDto.fetchAll();
    }

}

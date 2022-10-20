package com.increff.controller;

import com.increff.dto.BinDto;
import com.increff.exception.ApiException;
import com.increff.model.data.BinData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/bin")
public class BinController {
    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Creates no of bins")
    @RequestMapping(path = "/create/{noOfBins}", method = RequestMethod.POST)
    public List<BinData> add(@PathVariable Long noOfBins) throws ApiException {
        return binDto.create(noOfBins);
    }

    @ApiOperation(value ="Fetches list of all bins")
    @RequestMapping(path = "/viewAll",method = RequestMethod.GET)
    public List<BinData> getAll() throws ApiException{
        return binDto.getAll();
    }
}

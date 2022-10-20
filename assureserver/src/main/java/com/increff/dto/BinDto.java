package com.increff.dto;

import com.increff.dto.helper.BinDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.BinData;
import com.increff.pojo.BinPojo;
import com.increff.service.BinService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinDto {
    @Autowired
    private BinService binService;

    public List<BinData> create(Long noOfBins) throws ApiException {
        if(noOfBins <=0)
            throw new ApiException("Enter valid no. of bins");
        List<BinData> binList = new ArrayList<>();
        for(BinPojo pojo : binService.create(noOfBins)) {
            binList.add(BinDtoHelper.convertToData(pojo));
        }
        return binList;
    }

    public List<BinData> getAll() {
        List<BinData> binData = new ArrayList<>();
        for (BinPojo pojo : binService.getAllBins()) {
            binData.add(BinDtoHelper.convertToData(pojo));
        }
        return binData;
    }
}

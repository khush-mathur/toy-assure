package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.model.data.BinData;
import com.increff.pojo.BinPojo;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinDtoTest extends AbstractUnitTest {
    @Resource
    BinDto dto;

    @Test
    public void binCreate() throws ApiException {
        Long noOfBins = 5L;
        List<BinData> binList =  dto.create(noOfBins);
        assertEquals(5,binList.size());
    }

    @Test(expected = ApiException.class)
    public void invalidBinCreate() throws ApiException {
        Long noOfBins = -5L;
        try {
            List<BinData> binList =  dto.create(noOfBins);
        }
        catch (ApiException e){
            assertEquals("Enter valid no. of bins",e.getMessage());
            throw e;
        }
    }
    @Test
    public void getAll() throws ApiException {
        dto.create(5L);
        dto.create(6L);
        List<BinData> binList = dto.getAll();
        assertEquals(11,binList.size());
    }
}

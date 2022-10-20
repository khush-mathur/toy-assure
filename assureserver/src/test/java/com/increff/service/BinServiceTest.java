package com.increff.service;

import com.increff.AbstractUnitTest;
import com.increff.exception.ApiException;
import com.increff.pojo.BinPojo;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BinServiceTest extends AbstractUnitTest {

    @Resource
    BinService binService;

    @Test
    public void binCreate() {
        Long noOfBins = 5L;
        List<BinPojo> binList =  binService.create(noOfBins);
        assertEquals(5,binList.size());
    }

    @Test
    public void getAll(){
        binService.create(5L);
        binService.create(6L);
        List<BinPojo> binList = binService.getAllBins();
        assertEquals(11,binList.size());
    }
    @Test
    public void getById() throws ApiException {
        List<BinPojo> binList =binService.create(1L);
        BinPojo expectedBin = binList.get(0);
        BinPojo actualBin = binService.getByBinId(expectedBin.getBinId());
        assertEquals(expectedBin,actualBin);
    }
    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        List<BinPojo> binList =binService.create(1L);
        Long invalidBinId = new Random().nextLong();
        try {
            BinPojo actualBin = binService.getByBinId(invalidBinId);
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "No bin with id: " + invalidBinId + " exists.");
            throw e;
        }
    }

}

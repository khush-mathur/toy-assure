package com.increff.service;

import com.increff.dao.BinDao;
import com.increff.exception.ApiException;
import com.increff.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinService {
    @Autowired
    private BinDao binDao;

    @Transactional(rollbackFor = ApiException.class)
    public List<BinPojo> create(Long noOfBins) {
        List<BinPojo> binList = new ArrayList<>();
        for (long i = 0 ; i < noOfBins ; i++){
            binList.add(binDao.create(new BinPojo()));
        }
        return binList;
    }

    @Transactional(readOnly = true)
    public BinPojo getByBinId(Long binId) throws ApiException {
        BinPojo bin = binDao.getById(binId);
        if(bin == null)
            throw new ApiException("No bin with id: " + binId + " exists.");
        return bin;
    }

    @Transactional(readOnly = true)
    public List<BinPojo> getAllBins() {
        return binDao.selectAll();
    }
}

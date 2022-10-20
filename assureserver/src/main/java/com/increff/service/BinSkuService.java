package com.increff.service;

import com.increff.dao.BinSkuDao;
import com.increff.exception.ApiException;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;
import com.increff.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinSkuService {
    @Autowired
    private BinSkuDao binSkuDao;

    @Transactional(rollbackFor = ApiException.class)
    public BinSkuPojo add(BinSkuPojo inputBinSku) {
        BinSkuPojo binSkuPojo = binSkuDao.getByBinIdAndGSkuId(inputBinSku.getBinId(),inputBinSku.getGlobalSkuId());
        if(binSkuPojo!=null) {
            binSkuPojo.setQuantity(binSkuPojo.getQuantity() + inputBinSku.getQuantity());
            return binSkuPojo;
        }
        return binSkuDao.add(inputBinSku);
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getAllBinWise() {
        return binSkuDao.selectAllBinWise();
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getByGSkuId(Long gSkuId){
        return binSkuDao.getByGSkuId(gSkuId);
    }

    @Transactional(rollbackFor = ApiException.class)
    public BinSkuPojo update(Long id, BinSkuPojo inputBinSku) {
        BinSkuPojo existingBinSku = binSkuDao.getById(id);
        existingBinSku.setQuantity(inputBinSku.getQuantity());
        return existingBinSku;
    }

    @Transactional(readOnly = true)
    public BinSkuPojo get(Long binId , Long globalSkuId) throws ApiException {
        BinSkuPojo binSkuPojo = binSkuDao.getByBinIdAndGSkuId(binId,globalSkuId);
        if(binSkuPojo == null)
            throw new ApiException("No such sku in given bin exists.");
        return binSkuPojo;
    }

    @Transactional(rollbackFor = ApiException.class)
    public void allocate(OrderItemPojo item,Long needToAllocate) {
        for(BinSkuPojo binSku : getByGSkuId(item.getGlobalSkuId())){
            Long binWiseAllocate = Math.min(binSku.getQuantity(), needToAllocate);
            binSku.setQuantity(binSku.getQuantity() - binWiseAllocate);
            needToAllocate-=binWiseAllocate;
            if (needToAllocate == 0)
                return;
        }
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getById(Long id) throws ApiException {
        BinSkuPojo binSkuPojo = binSkuDao.getById(id);
        if(binSkuPojo == null)
            throw new ApiException("No such bin-sku exists.");
        return binSkuPojo;
    }
}

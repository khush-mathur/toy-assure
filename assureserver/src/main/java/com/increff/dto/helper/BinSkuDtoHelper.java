package com.increff.dto.helper;

import com.increff.model.data.BinSkuData;
import com.increff.model.data.InventoryData;
import com.increff.model.form.BinSkuForm;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;

public class BinSkuDtoHelper {
    public static BinSkuPojo convertToPojo(BinSkuForm binSku){
        BinSkuPojo pojo = new BinSkuPojo();
        pojo.setBinId(binSku.getBinId());
        pojo.setQuantity(binSku.getQuantity());
        return pojo;
    }

    public static BinSkuData convertToData(BinSkuPojo pojo) {
        BinSkuData data = new BinSkuData();
        data.setId(pojo.getId());
        data.setBinId(pojo.getBinId());
        data.setQuantity(pojo.getQuantity());
        return data;
    }

}

package com.increff.dto.helper;

import com.increff.model.data.BinData;
import com.increff.pojo.BinPojo;

public class BinDtoHelper {
    public static BinData convertToData(BinPojo pojo) {
        BinData bin = new BinData();
        bin.setBinId(pojo.getBinId());
        return bin;
    }
}

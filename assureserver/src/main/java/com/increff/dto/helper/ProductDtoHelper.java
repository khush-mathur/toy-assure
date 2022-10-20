package com.increff.dto.helper;

import com.increff.model.data.ProductData;
import com.increff.model.form.ProductForm;
import com.increff.model.retrundata.ProductReturnData;
import com.increff.pojo.ProductPojo;

public class ProductDtoHelper {
    public static ProductPojo convertToProductPojo(ProductForm product) {
        ProductPojo pojo = new ProductPojo();
        pojo.setName(product.getName());
        pojo.setMrp(Double.valueOf(product.getMrp()));
        pojo.setDescription(product.getDescription());
        pojo.setClientSkuId(product.getClientSkuId());
        pojo.setBrand(product.getBrand());
        return pojo;
    }

    public static ProductData convertToData(ProductPojo pojo) {
        ProductData product = new ProductData();
        product.setName(pojo.getName());
        product.setMrp(pojo.getMrp().toString());
        product.setDescription(pojo.getDescription());
        product.setClientSkuId(pojo.getClientSkuId());
        product.setBrand(pojo.getBrand());
//        product.setClientId(pojo.getClientId());
        product.setGlobalSkuId(pojo.getGlobalSkuId());
        return product;
    }
    public static ProductReturnData convertToReturnData(ProductForm form) {
        ProductReturnData product = new ProductReturnData();
        product.setName(form.getName());
        product.setMrp(form.getMrp());
        product.setDescription(form.getDescription());
        product.setClientSkuId(form.getClientSkuId());
        product.setBrand(form.getBrand());
        return product;
    }
}

package com.increff.service;

import com.increff.dao.ProductDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackFor = ApiException.class)
    public void create(ProductPojo productPojo) throws ApiException {
        if (productAlreadyExists(productPojo))
            throw new ApiException("Product "+ productPojo.getName()  + " already exists");
        productDao.add(productPojo);
    }

    @Transactional(rollbackFor = ApiException.class)
    public ProductPojo update(Long clientId, ProductPojo inputProduct) throws ApiException{
        ProductPojo existingProduct = productDao.getByClientIdNSkuId(clientId,inputProduct.getClientSkuId());
        if(existingProduct==null)
            throw new ApiException("No product exists");
        existingProduct.setMrp(inputProduct.getMrp());
        existingProduct.setName(inputProduct.getName());
        existingProduct.setBrand(inputProduct.getBrand());
        existingProduct.setDescription(inputProduct.getDescription());
        return existingProduct;
    }
    @Transactional(readOnly = true)
    public ProductPojo getProductByGSkuId(Long gSkuId) throws ApiException {
        ProductPojo product = productDao.getByGSkuID(gSkuId);
        if(product == null)
            throw new ApiException("No such product exists.");
        return product;
    }

    @Transactional(readOnly = true)
    public ProductPojo getProductByClientIdNSkuId(Long clientId,String clientSku) throws ApiException {
        ProductPojo product = productDao.getByClientIdNSkuId(clientId,clientSku);
        if(product == null)
            throw new ApiException("No such product exists.");
        return product;
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll(){
       return  productDao.getAll();
    }

    private boolean productAlreadyExists(ProductPojo inputProduct) throws ApiException{
        if(productDao.getByClientIdNSkuId(inputProduct.getClientId(),inputProduct.getClientSkuId())!=null)
            return true;
        return false;
    }
}

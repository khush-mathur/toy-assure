package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private final static String SELECT_BY_GSKUID = "select d from assure_product d where globalSkuId = :gSkuId";
    private final static String SELECT_BY_CLIENTID_N_CSKUID = "select d from assure_product d where clientId = :cId and clientSkuId = :cSkuId";
    private final static String SELECT_BY_BARCODE = "select d from assure_product d where barcode = :barcode";
    private final static String SELECT_ALL = "select d from assure_product d";

    @Transactional(rollbackFor = ApiException.class)
    public void add(ProductPojo product) {
        em().persist(product);
    }

    @Transactional(readOnly = true)
    public ProductPojo getByGSkuID(Long globalSkuId) {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_BY_GSKUID,ProductPojo.class);
        query.setParameter("gSkuId",globalSkuId);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAll() {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_ALL,ProductPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ProductPojo getByClientIdNSkuId(Long clientId, String clientSkuId) {
        TypedQuery<ProductPojo> query = em().createQuery(SELECT_BY_CLIENTID_N_CSKUID,ProductPojo.class);
        query.setParameter("cId",clientId);
        query.setParameter("cSkuId",clientSkuId);
        return getSingleRow(query);
    }
}

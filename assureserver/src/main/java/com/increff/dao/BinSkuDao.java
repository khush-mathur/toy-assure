package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.BinSkuPojo;
import com.increff.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

    private final static String SELECT_BY_BINID_GSKUID = "select u from assure_bin_sku u where binId  = :binId and globalSkuId = :globalSkuId";
    private final static String SELECT_BY_ID = "select u from assure_bin_sku u where id = :id";
    private static final String SELECT_ALL_BIN_WISE = "select u from assure_bin_sku u";
    private final static String SELECT_ALL_BY_SKU_ID = "select u from assure_bin_sku u where globalSkuId = :globalSkuId";

    @Transactional(rollbackFor = ApiException.class)
    public BinSkuPojo add(BinSkuPojo pojo) {
        em().persist(pojo);
        return pojo;
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getByBinIdAndGSkuId(Long binId, Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_BINID_GSKUID, BinSkuPojo.class);
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getById(Long id) {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_ID, BinSkuPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> selectAllBinWise() {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_ALL_BIN_WISE, BinSkuPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<BinSkuPojo> getByGSkuId(Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_ALL_BY_SKU_ID, BinSkuPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList();
    }

}

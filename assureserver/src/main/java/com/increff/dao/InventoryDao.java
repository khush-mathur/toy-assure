package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private final static String SELECT_BY_GSKUID = "select u from assure_inventory u where globalSkuId  = :globalSkuId";
    private static final String SELECT_ALL = "select u from assure_inventory u";

    @Transactional(rollbackFor = ApiException.class)
    public InventoryPojo create(InventoryPojo user) {
        em().persist(user);
        return user;
    }

    @Transactional(readOnly = true)
    public InventoryPojo get(Long globalSkuId) {
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_BY_GSKUID, InventoryPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = em().createQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }
}

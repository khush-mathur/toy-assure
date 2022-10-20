package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.BinPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao {

    private final static String SELECT_ALL = "select d from assure_bin d";
    private final static String SELECT_BY_ID = "select d from assure_bin d where binId = :binId";

    @Transactional(rollbackFor = ApiException.class)
    public BinPojo create(BinPojo binPojo) {
        em().persist(binPojo);
        return binPojo;
    }

    @Transactional(readOnly = true)
    public List<BinPojo> selectAll() {
        TypedQuery<BinPojo> query = em().createQuery(SELECT_ALL,BinPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public BinPojo getById(Long binId) {
        TypedQuery<BinPojo> query = em().createQuery(SELECT_BY_ID,BinPojo.class);
        query.setParameter("binId",binId);
        return getSingleRow(query);
    }
}

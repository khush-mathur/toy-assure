package com.increff.abstractDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class AbstractDao {
    @PersistenceContext
    private EntityManager em;

    protected EntityManager em(){
        return this.em;
    }

    protected <T> T getSingleRow(TypedQuery<T> query) {
        return query.getResultList().stream().findFirst().orElse(null);
    }
}

package com.increff.dao;

import com.increff.abstractDao.AbstractDao;
import com.increff.exception.ApiException;
import com.increff.pojo.UserPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {

    private final static String SELECT_BY_ID = "select u from assure_user u where id  = :id";
    private static final String SELECT_ALL = "select u from assure_user u order by type";
    private static final String SELECT_BY_NAME = "select u from assure_user u where name  = :name";
    private static final String SELECT_ALL_CLIENTS = "select u from assure_user u where type = 'CLIENT'";
    private static final String SELECT_ALL_CUSTOMERS = "select u from assure_user u where type = 'CUSTOMER'";
    @Transactional(readOnly = true)
    public UserPojo getById(Long id) {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_ID, UserPojo.class);
        query.setParameter("id", id);
        return getSingleRow(query);
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo create(UserPojo user) {
        em().persist(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<UserPojo> selectAll() {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_ALL, UserPojo.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public UserPojo getByName(String name) {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_NAME, UserPojo.class);
        query.setParameter("name", name);
        return getSingleRow(query);
    }

    @Transactional(readOnly = true)
    public List<UserPojo> selectAllClients() {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_ALL_CLIENTS, UserPojo.class);
        return query.getResultList();
    }

    public List<UserPojo> selectAllCustomers() {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_ALL_CUSTOMERS, UserPojo.class);
        return query.getResultList();
    }
}

package com.increff.service;

import com.increff.dao.UserDao;
import com.increff.exception.ApiException;
import com.increff.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public UserPojo getById(Long id) throws ApiException {
        UserPojo user = userDao.getById(id);
        if(user == null)
            throw new ApiException("No such user exists.");
        return user;
    }

    @Transactional(readOnly = true)
    public UserPojo getByName(String name) throws ApiException {
        UserPojo user = userDao.getByName(name);
        return user;
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo create(UserPojo pojo) {
        return userDao.create(pojo);
    }

    @Transactional(readOnly = true)
    public List<UserPojo> getAllUsers() {
        return userDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public UserPojo update(Long id,UserPojo pojo) {
        UserPojo existing = userDao.getById(id);
        existing.setName(pojo.getName());
        existing.setType(pojo.getType());
        return existing;
    }

    @Transactional(readOnly = true)
    public boolean isClient(Long id) {
        UserPojo pojo = userDao.getById(id);
        if(pojo==null || !pojo.getType().toString().equals("CLIENT"))
            return false;
        return true;
    }
    @Transactional(readOnly = true)
    public boolean isClient(String name) {
        UserPojo pojo = userDao.getByName(name);
        if(pojo==null || !pojo.getType().toString().equals("CLIENT"))
            return false;
        return true;
    }

    @Transactional(readOnly = true)
    public boolean isCustomer(Long id) {
        UserPojo pojo = userDao.getById(id);
        if(pojo==null || !pojo.getType().toString().equals("CUSTOMER"))
            return false;
        return true;
    }
    @Transactional(readOnly = true)
    public boolean isCustomer(String name) {
        UserPojo pojo = userDao.getByName(name);
        if(pojo==null || !pojo.getType().toString().equals("CUSTOMER"))
            return false;
        return true;
    }

    @Transactional(readOnly = true)
    public List<UserPojo> getAllClients() {
        return userDao.selectAllClients();
    }

    @Transactional(readOnly = true)
    public List<UserPojo> getAllCustomers() {
        return userDao.selectAllCustomers();
    }
}

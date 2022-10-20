package com.increff.dto;

import com.increff.AbstractUnitTest;
import com.increff.enums.UserType;
import com.increff.exception.ApiException;
import com.increff.model.data.UserData;
import com.increff.model.form.UserForm;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class UserDtoTest extends AbstractUnitTest {

    @Resource
    UserDto dto;
    private final static String clientType = "CLIENT";
    private final static String customerType = "CUSTOMER";
    @Test
    public void createUser() throws ApiException {
        String clientName = RandomStringUtils.random(6);
        String customerName = RandomStringUtils.random(6);
        UserData client = dto.create(createUserForm(clientName,clientType));
        UserData customer = dto.create(createUserForm(customerName,customerType));
        assertEquals(clientName.trim().toLowerCase(),client.getName());
        assertEquals(customerName.trim().toLowerCase(),customer.getName());
        assertEquals(clientType,client.getType());
        assertEquals(customerType,customer.getType());
    }

    @Test(expected = ApiException.class)
    public void createExistingUser() throws ApiException {
        String userName = RandomStringUtils.random(6);
        dto.create(createUserForm(userName,clientType));
        try {
            UserData user = dto.create(createUserForm(userName, customerType));
        }
        catch(ApiException e){
            assertEquals("User already exists.",e.getMessage());
            throw e;
        }
    }

    @Test(expected = ApiException.class)
    public void createWithInvalidUserName() throws ApiException {
        try {
            dto.create(createUserForm("", clientType));
        }
        catch(ApiException e){
            assertEquals("Invalid Input : name cannot be empty",e.getMessage());
            throw e;
        }

    }

    @Test(expected = ApiException.class)
    public void createWithInvalidUserType() throws ApiException {
        try {
            dto.create(createUserForm(RandomStringUtils.random(6), "clientType"));
        }
        catch(ApiException e){
            assertEquals("Invalid input : Enter a valid Type",e.getMessage());
            throw e;
        }
    }

    @Test
    public void getById() throws ApiException {
        UserData createUser = dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        UserData fetchedUser = dto.getById(createUser.getId());
        assertEquals(createUser.getName(),fetchedUser.getName());
        assertEquals(createUser.getType(),fetchedUser.getType());
    }

    @Test(expected = ApiException.class)
    public void getByInvalidId() throws ApiException {
        UserData createUser = dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        try {
            dto.getById(new Random().nextLong());
        }
        catch (ApiException e){
            assertEquals("No such user exists.",e.getMessage());
            throw e;
        }
    }

    @Test
    public void getAll() throws ApiException {
        for(int i =0;i<3;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        }
        for(int i =0;i<4;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),customerType));
        }
        List<UserData> userList = dto.fetchAll();
        assertEquals(7,userList.size());
    }

    @Test
    public void getAllCustomers() throws ApiException {
        for(int i =0;i<3;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        }
        for(int i =0;i<4;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),customerType));
        }
        List<UserData> customerList = dto.fetchAllCustomers();
        assertEquals(4,customerList.size());
    }

    @Test
    public void getAllClients() throws ApiException {
        for(int i =0;i<3;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        }
        for(int i =0;i<4;i++){
            dto.create(createUserForm(RandomStringUtils.random(6),customerType));
        }
        List<UserData> clientsList = dto.fetchAllClients();
        assertEquals(3,clientsList.size());
    }

    @Test
    public void updateUser() throws ApiException {
        UserData existingUser = dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        String newName = RandomStringUtils.random(6);
        UserData updatedUser = dto.update(existingUser.getId(),createUserForm(newName,customerType));
        assertEquals(newName,updatedUser.getName());
        assertEquals(customerType,updatedUser.getType());
    }

    @Test(expected = ApiException.class)
    public void updateInvalidUser() throws ApiException {
        UserData existingUser = dto.create(createUserForm(RandomStringUtils.random(6),clientType));
        String newName = RandomStringUtils.random(6);
        try {
            UserData updatedUser = dto.update(new Random().nextLong(), createUserForm(newName, customerType));
        }
        catch (ApiException e){
            assertEquals("No such user exists.",e.getMessage());
            throw e;
        }
    }
    private UserForm createUserForm(String name, String type){
        return new UserForm().builder().name(name).type(type).build();
    }
}

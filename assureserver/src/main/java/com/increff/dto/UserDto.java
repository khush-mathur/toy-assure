package com.increff.dto;

import com.increff.dto.helper.UserDtoHelper;
import com.increff.exception.ApiException;
import com.increff.model.data.UserData;
import com.increff.model.form.UserForm;
import com.increff.pojo.UserPojo;
import com.increff.enums.UserType;
import com.increff.service.UserService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDto {
    @Autowired
    private UserService userService;
    public UserData getById(Long id) throws ApiException {
        UserPojo pojo = userService.getById(id);
        return UserDtoHelper.convertToData(pojo);
    }

    public UserData create(UserForm rawForm) throws ApiException {
        UserForm form = normalise(rawForm);
        if(!validate(form) || userService.getByName(form.getName())!=null) {
            throw new ApiException("User already exists.");
        }
        UserPojo pojo = userService.create(UserDtoHelper.convertToPojo(form));
        return UserDtoHelper.convertToData(pojo);
    }

    public List<UserData> fetchAll() {
        List<UserData> userData = new ArrayList<>();
        for (UserPojo pojo : userService.getAllUsers()) {
            userData.add(UserDtoHelper.convertToData(pojo));
        }
        return userData;
    }
    public List<UserData> fetchAllClients() {
        List<UserData> userData = new ArrayList<>();
        for (UserPojo pojo : userService.getAllClients()) {
            userData.add(UserDtoHelper.convertToData(pojo));
        }
        return userData;
    }

    public UserData update(Long id, UserForm rawUserForm) throws ApiException {
        UserForm userForm = normalise(rawUserForm);
        if (!validate(userForm) || userService.getById(id)==null){
            throw new ApiException("No such user exist.");
        }
        UserPojo user = userService.update(id,UserDtoHelper.convertToPojo(userForm));
        return UserDtoHelper.convertToData(user);
    }
    public List<UserData> fetchAllCustomers() {
        List<UserData> userData = new ArrayList<>();
        for (UserPojo pojo : userService.getAllCustomers()) {
            userData.add(UserDtoHelper.convertToData(pojo));
        }
        return userData;
    }
    private boolean validate(UserForm form) throws ApiException {
        if(form.getName()==null || form.getName().isEmpty())
            throw new ApiException("Invalid Input : name cannot be empty");
        else if(form.getType()==null ||form.getType().isEmpty() || !EnumUtils.isValidEnum(UserType.class, form.getType()))
            throw new ApiException("Invalid input : Enter a valid Type");
        return true;
    }
    private UserForm normalise(UserForm user){
        user.setName(user.getName().trim().toLowerCase());
        user.setType(user.getType().trim().toUpperCase());
        return user;
    }


}

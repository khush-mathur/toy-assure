package com.increff.dto.helper;

import com.increff.model.data.UserData;
import com.increff.model.form.UserForm;
import com.increff.pojo.UserPojo;
import com.increff.enums.UserType;

public class UserDtoHelper {

    public static UserData convertToData(UserPojo pojo){
        UserData user = new UserData();
        user.setId(pojo.getId());
        user.setName(pojo.getName());
        user.setType(pojo.getType().toString());
        return user;
    }

    public static UserPojo convertToPojo(UserForm form) {
        UserPojo pojo = new UserPojo();
        pojo.setName(form.getName());
        pojo.setType(UserType.valueOf(form.getType()));
        return pojo;
    }
}

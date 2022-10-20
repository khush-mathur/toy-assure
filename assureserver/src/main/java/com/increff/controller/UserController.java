package com.increff.controller;

import com.increff.dto.UserDto;
import com.increff.exception.ApiException;
import com.increff.model.data.UserData;
import com.increff.model.form.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "Get a user by Id")
    @RequestMapping(path = "/view/{id}",method = RequestMethod.GET)
    public UserData get(@PathVariable Long id) throws ApiException {
        return userDto.getById(id);
    }

    @ApiOperation(value = "Creates a new User")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public UserData add(@RequestBody UserForm form) throws ApiException {
        return userDto.create(form);
    }

    @ApiOperation(value = "Gets list of all Users")
    @RequestMapping(path = "/viewAll", method = RequestMethod.GET)
    public List<UserData> getAll() {
        return userDto.fetchAll();
    }

    @ApiOperation(value = "Gets list of all Clients")
    @RequestMapping(path = "/viewAllClients", method = RequestMethod.GET)
    public List<UserData> getAllClients() {
        return userDto.fetchAllClients();
    }

    @ApiOperation(value = "Gets list of all Customers")
    @RequestMapping(path = "/viewAllCustomers", method = RequestMethod.GET)
    public List<UserData> getAllCustomers() {
        return userDto.fetchAllCustomers();
    }

    @ApiOperation(value = "Update a user by id")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public UserData update(@PathVariable Long id, @RequestBody UserForm brandForm) throws ApiException {
        return userDto.update(id, brandForm);
    }
}

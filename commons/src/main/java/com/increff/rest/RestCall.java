package com.increff.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.model.data.OrderData;
import com.increff.model.form.ChannOrderForm;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class RestCall {
    public OrderData createOrder(ChannOrderForm form, String url, HttpMethod method) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String restBody = new ObjectMapper().writeValueAsString(form);
        HttpEntity<String> entity = new HttpEntity<>(restBody,headers);
        return restTemplate.exchange(url, method, entity, OrderData.class).getBody();
    }
    public File getChannelInvoice(String url, HttpMethod method) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
//        String restBody = new ObjectMapper().writeValueAsString(orderId);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, method, entity, File.class).getBody();
    }
}

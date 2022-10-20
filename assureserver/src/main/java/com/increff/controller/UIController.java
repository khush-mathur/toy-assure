package com.increff.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/ui/home")
    public ModelAndView home() {
        return mav("home.html");
    }

    @RequestMapping(value = "/ui/user")
    public ModelAndView brand() {
        return mav("user.html");
    }

    @RequestMapping(value = "/ui/product")
    public ModelAndView product() {
        return mav("product.html");
    }

    @RequestMapping(value = "/ui/order")
    public ModelAndView order() {
        return mav("orders.html");
    }

    @RequestMapping(value = "/ui/inventory")
    public ModelAndView inventory() {
        return mav("inventory.html");
    }

    @RequestMapping(value = "/ui/bin")
    public ModelAndView bin() {
        return mav("bin.html");
    }

    @RequestMapping(value = "/ui/channel")
    public ModelAndView channel() {
        return mav("channel.html");
    }

    @RequestMapping(value = "/ui/bin-sku")
    public ModelAndView binSku() {
        return mav("bin-sku.html");
    }
    @RequestMapping(value = "/ui/channel-listing")
    public ModelAndView channelListing() {
        return mav("channel-listing.html");
    }

    private ModelAndView mav(String page) {

        ModelAndView mav = new ModelAndView(page);
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}

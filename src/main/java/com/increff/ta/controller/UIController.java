package com.increff.ta.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/ui/home")
    public ModelAndView home() {
        return mav("home.html");
    }

    @RequestMapping(value = "/ui/features")
    public ModelAndView features() {
        return mav("features.html");
    }

    @RequestMapping(value = "/ui/pricing")
    public ModelAndView pricing() {
        return mav("pricing.html");
    }

    private ModelAndView mav(String page) {
        ModelAndView mav = new ModelAndView(page);
//		mav.addObject("info", new InfoData());
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }

}

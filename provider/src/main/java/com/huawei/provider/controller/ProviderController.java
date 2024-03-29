package com.huawei.provider.controller;

import com.netflix.config.DynamicPropertyFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
    @RequestMapping("/hello")
    public String sayHello(@RequestParam("name") String name) {
        return "hello world"+name;
    }
    @RequestMapping("/config")
    public String config() {
        return DynamicPropertyFactory.getInstance().getStringProperty("name", null).get();
    }
}

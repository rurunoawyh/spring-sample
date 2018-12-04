package com.wyh.spring.applicationeventdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午1:04
 */
@RestController
public class RegisterController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @RequestMapping("/register")
    public String register(String name, String password) {
        RegisterEvent registerEvent = new RegisterEvent(name + ":" + password);
        applicationEventPublisher.publishEvent(registerEvent);
        return "success";
    }
}

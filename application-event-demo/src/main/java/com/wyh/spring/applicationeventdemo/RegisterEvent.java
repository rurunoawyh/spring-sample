package com.wyh.spring.applicationeventdemo;

import org.springframework.context.ApplicationEvent;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午1:14
 */
public class RegisterEvent extends ApplicationEvent {
    private String message;

    public RegisterEvent(Object source) {
        super(source);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

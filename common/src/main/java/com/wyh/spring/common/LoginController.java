package com.wyh.spring.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wb-wyh270612
 * @date 2018/12/4  下午12:32
 */
@RestController
public class LoginController {
    @RequestMapping("/login")
    public Object login() {
        List<Object> objects = null;
        return objects.get(0);
    }
}

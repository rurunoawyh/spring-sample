package com.wyh.spring.applicationeventdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午1:40
 */
@Component
@Slf4j
public class SmsListener implements ApplicationListener<RegisterEvent> {
    @Override
    public void onApplicationEvent(RegisterEvent registerEvent) {
        log.info("接受到事件：{}", registerEvent);
        log.info("发送短信。。。。");
    }
}

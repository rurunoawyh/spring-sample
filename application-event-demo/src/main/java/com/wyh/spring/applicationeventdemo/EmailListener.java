package com.wyh.spring.applicationeventdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wb-wyh270612
 * @date 2018/12/3  下午1:17
 */
@Component
@Slf4j
public class EmailListener implements ApplicationListener<RegisterEvent> {
    @Override
    public void onApplicationEvent(RegisterEvent registerEvent) {
        log.info("接受到事件：{}", registerEvent);
        // 发送邮件
        log.info("发送邮件。。。。");
    }
}

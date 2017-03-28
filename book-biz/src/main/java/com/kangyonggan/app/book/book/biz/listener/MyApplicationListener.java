package com.kangyonggan.app.book.book.biz.listener;

import com.kangyonggan.app.book.book.biz.service.RedisService;
import com.kangyonggan.app.book.book.biz.util.PropertiesUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
@Component
@Log4j2
public class MyApplicationListener implements ApplicationListener {

    @Autowired
    private RedisService redisService;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        redisService.delete(PropertiesUtil.getProperties("redis.prefix") + ":" + "bookEngine");
        log.info("清除书籍引擎执行标致成功");
        redisService.delete(PropertiesUtil.getProperties("redis.prefix") + ":" + "chapterEngine");
        log.info("清除章节引擎执行标致成功");
    }
}

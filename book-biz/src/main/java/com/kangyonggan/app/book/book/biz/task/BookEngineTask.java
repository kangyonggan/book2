package com.kangyonggan.app.book.book.biz.task;

import com.kangyonggan.app.book.book.biz.service.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 书籍引擎, 定时爬取新的数据
 *
 * @author kangyonggan
 * @since 2017/3/27
 */
@Component
@Log4j2
public class BookEngineTask {

    @Autowired
    private BookService bookService;

    /**
     * 每天凌晨 02:00 执行一次
     * cron表达式：* * * * * *（秒 分 时 日 月 星期）
     */
    @Scheduled(cron = "0 0 2 * * *")// 线上使用, 其实应该写入配置的
//    @Scheduled(cron = "0/10 * * * * *")// 调试时使用
    public void autoCardCalculate() {
        bookService.execute();
    }

}

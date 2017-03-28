package com.kangyonggan.app.book.book.biz.task;

import com.kangyonggan.app.book.book.biz.service.BookService;
import com.kangyonggan.app.book.book.biz.service.RedisService;
import com.kangyonggan.app.book.book.biz.util.HtmlUtil;
import com.kangyonggan.app.book.book.biz.util.PropertiesUtil;
import com.kangyonggan.app.book.book.model.vo.Book;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    /**
     * 全部书籍路径
     */
    private static final String PATH_ALL = "http://zhannei.baidu.com/cse/search?q=&p=%d&s=11869390265411396408";

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisService redisService;

    /**
     * 每天凌晨 02:00 执行一次
     * cron表达式：* * * * * *（秒 分 时 日 月 星期）
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void execute() {
        // 判断是否重复执行
        if (isExecuting()) {
            log.warn("书籍执行引擎正在执行中, 不可重复执行!");
            return;
        }
        log.info("书籍引擎已经开始执行...");

        // 执行标致放入redis
        redisService.set(PropertiesUtil.getProperties("redis.prefix") + ":" + "bookEngine", "true");

        // 抓取的当前页数
        int pageNum = 0;

        while (true) {
            // 抓取第pageNum页
            Document doc = HtmlUtil.parseUrl(String.format(PATH_ALL, pageNum));
            if (doc == null) {
                log.info("抓取的第{}页书籍为空，结束抓取!", pageNum + 1);
                redisService.delete(PropertiesUtil.getProperties("redis.prefix") + ":" + "bookEngine");
                log.info("清除书籍引擎执行标致成功");
                break;
            }
            pageNum++;

            // 书籍列表
            Elements bookElements;
            try {
                bookElements = doc.select("#results .result-list .result-item");
            } catch (Exception e) {
                log.error("抓取第" + pageNum + "页的书籍列表失败, 抓取下一页", e);
                continue;
            }
            log.info("在第{}页抓取了{}篇书籍", pageNum, bookElements.size());

            for (int i = 1; i < bookElements.size(); i++) {
                // 抓取每本书的详细信息
                Book book = parseBook(bookElements.get(i));

                log.info("解析后的书籍:{}", book);
                if (book != null) {
                    // 判断书籍是否已经存在
                    boolean isExist = bookService.exist(book.getUrl());
                    log.info("书籍【" + book.getName() + (isExist ? "】存在" : "】不存在"));

                    if (!isExist) {
                        try {
                            bookService.saveBook(book);
                            log.info("书籍【{}】落库成功", book.getName());
                        } catch (Exception e) {
                            log.error("书籍【" + book.getName() + "】落库异常", e);
                        }
                    }
                }
            }
        }

        redisService.delete(PropertiesUtil.getProperties("redis.prefix") + ":" + "bookEngine");
        log.info("清除书籍引擎执行标致成功");
        log.info("书籍引擎已经结束执行...");
    }

    /**
     * 判断书籍执行引擎是否正在执行
     *
     * @return
     */
    public synchronized boolean isExecuting() {
        Object bookEngineRedis = redisService.get(PropertiesUtil.getProperties("redis.prefix") + ":" + "bookEngine");
        if (bookEngineRedis == null) {
            return false;
        }
        return true;
    }

    /**
     * 抓取书的详细信息
     *
     * @param bookElement
     * @return
     */
    private Book parseBook(Element bookElement) {
        try {
            String picture = bookElement.select(".result-game-item-pic a img").get(0).attr("src").trim();
            String intro = bookElement.select(".result-game-item-desc").get(0).text().trim();

            Element element = bookElement.select(".result-game-item-detail h3 a").get(0);
            String name = element.text().trim();
            String url = element.attr("href");
            url = url.substring(url.lastIndexOf("book/") + 5).replaceAll("/", "").trim();

            Elements info = bookElement.select(".result-game-item-info .result-game-item-info-tag");
            String author = info.get(0).select("span").get(1).text().trim();
            String type = info.get(1).select("span").get(1).text().trim();
            String newChapterTitle = info.get(3).select("a").get(0).text().trim();

            Book book = new Book();
            book.setPicture(picture);
            book.setName(name);
            book.setUrl(url);
            book.setIntro(intro);
            book.setAuthor(author);
            book.setType(type);
            book.setNewChapterTitle(newChapterTitle);
            book.setNewChapterUrl(null);

            return book;
        } catch (Exception e) {
            log.error("抓取书籍详细信息异常" + bookElement, e);
        }
        return null;
    }
}

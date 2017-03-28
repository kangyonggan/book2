package com.kangyonggan.app.book.book.biz.task;

import com.kangyonggan.app.book.book.biz.service.BookService;
import com.kangyonggan.app.book.book.biz.service.ChapterService;
import com.kangyonggan.app.book.book.biz.service.RedisService;
import com.kangyonggan.app.book.book.biz.util.HtmlUtil;
import com.kangyonggan.app.book.book.biz.util.PropertiesUtil;
import com.kangyonggan.app.book.book.model.vo.Book;
import com.kangyonggan.app.book.book.model.vo.Chapter;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 章节引擎, 定时爬取新的数据
 *
 * @author kangyonggan
 * @since 2017/3/28
 */
@Component
@Log4j2
public class ChapterEngineTask {

    /**
     * 站点跟路径
     */
    private static final String PATH_BASE = "http://www.biquge.cn/";

    @Autowired
    private BookService bookService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private RedisService redisService;

    /**
     * 每天凌晨 03:00 执行一次
     * cron表达式：* * * * * *（秒 分 时 日 月 星期）
     */
    @Scheduled(cron = "0 0 3 * * *")
//    @Scheduled(cron = "0/5 * * * * *")
    public void execute() {
        // 判断是否重复执行
        if (isExecuting()) {
            log.warn("章节执行引擎正在执行中, 不可重复执行!");
            return;
        }
        log.info("章节引擎已经开始执行...");

        // 执行标致放入redis
        redisService.set(PropertiesUtil.getProperties("redis.prefix") + ":" + "chapterEngine", "true");

        int pageNum = 1;
        // 去库里捞书籍，每次捞10本
        while (true) {
            List<Book> books = bookService.findBooksByPage(pageNum++);
            if (books.isEmpty()) {
                log.info("已经捞完了库里的书籍，退出循环");
                break;
            }

            // 循环处理每本书
            for (Book book : books) {
                log.info("正在爬去书籍【{}({})】的所有章节...", book.getName(), book.getUrl());
                processBook(book);
            }
        }

        redisService.delete(PropertiesUtil.getProperties("redis.prefix") + ":" + "chapterEngine");
        log.info("清除章节引擎执行标致成功");
        log.info("章节引擎已经结束执行！！！");
    }

    /**
     * 爬取书籍的所有章节
     *
     * @param book
     */
    private void processBook(Book book) {
        Document bookDoc = HtmlUtil.parseUrl(PATH_BASE + "book/" + book.getUrl());
        if (bookDoc == null) {
            log.error("书籍【{}({})】的列表抓取失败！", book.getName(), book.getUrl());
            return;
        }

        // 章节列表
        Elements chapters = bookDoc.select("#list dl dd a");

        // 变量章节列表
        for (int i = 0; i < chapters.size(); i++) {
            Element chapter = chapters.get(i);
            String chapterUrl = chapter.attr("href");
            chapterUrl = chapterUrl.substring(chapterUrl.lastIndexOf("/") + 1, chapterUrl.lastIndexOf("."));

            processChapter(Integer.parseInt(chapterUrl), book);
        }

        // 最新章节校正
        Chapter newChapter = chapterService.findBookNewChapter(book.getUrl());
        if (newChapter != null) {
            book.setNewChapterUrl(newChapter.getUrl());
            book.setNewChapterTitle(newChapter.getTitle());

            bookService.updateBook(book);
            log.info("书籍【{}】的最新章节更新为{}", book.getName(), newChapter.getTitle());
        }
    }

    /**
     * 抓取书籍的某个章节
     *
     * @param chapterUrl
     * @param book
     * @return
     */
    private void processChapter(Integer chapterUrl, Book book) {
        // 防重复
        if (chapterService.exist(book.getUrl(), chapterUrl)) {
            log.info("书籍【{}】的章节{}已存在", book.getName(), chapterUrl);
            return;
        }

        Document chapterDoc = HtmlUtil.parseUrl(PATH_BASE + "book/" + book.getUrl() + "/" + chapterUrl + ".html");
        if (chapterDoc == null) {
            log.error("【{}】书的{}章节抓取失败", book.getName(), chapterUrl);
            return;
        }

        String title = chapterDoc.select(".bookname h1").html().trim();
        String content = chapterDoc.getElementById("content").html();

        Chapter chapter = new Chapter();
        chapter.setTitle(title);
        chapter.setContent(content);
        chapter.setBookUrl(book.getUrl());
        chapter.setUrl(chapterUrl);

        chapterService.saveChapter(chapter);
        log.info("【{}】书的{}章节已落库{}", book.getName(), chapterUrl, chapter);
    }

    /**
     * 判断章节执行引擎是否正在执行
     *
     * @return
     */
    public boolean isExecuting() {
        Object chapterEngineRedis = redisService.get(PropertiesUtil.getProperties("redis.prefix") + ":" + "chapterEngine");
        if (chapterEngineRedis == null) {
            return false;
        }
        return true;
    }
}

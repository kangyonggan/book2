package com.kangyonggan.app.book.book.biz.service;

import com.kangyonggan.app.book.book.model.vo.Chapter;

/**
 * @author kangyonggan
 * @since 2017/3/28
 */
public interface ChapterService {

    /**
     * 保存章节
     *
     * @param chapter
     */
    void saveChapter(Chapter chapter);

    /**
     * 判断章节是否存在
     *
     * @param bookUrl
     * @param chapterUrl
     * @return
     */
    boolean exist(Integer bookUrl, Integer chapterUrl);

    /**
     * 查找最新章节
     *
     * @param bookUrl
     * @return
     */
    Chapter findBookNewChapter(Integer bookUrl);
}

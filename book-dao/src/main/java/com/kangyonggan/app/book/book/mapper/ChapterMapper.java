package com.kangyonggan.app.book.book.mapper;

import com.kangyonggan.app.book.book.model.vo.Chapter;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterMapper extends MyMapper<Chapter> {

    /**
     * 查找最新章节
     *
     * @param bookUrl
     * @return
     */
    Chapter selectNewChapter(Integer bookUrl);
}
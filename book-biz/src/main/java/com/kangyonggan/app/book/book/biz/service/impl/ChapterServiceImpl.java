package com.kangyonggan.app.book.book.biz.service.impl;

import com.kangyonggan.app.book.book.biz.service.ChapterService;
import com.kangyonggan.app.book.book.mapper.ChapterMapper;
import com.kangyonggan.app.book.book.model.vo.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/3/28
 */
@Service
@Transactional
public class ChapterServiceImpl extends BaseService<Chapter> implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public void saveChapter(Chapter chapter) {
        super.insertSelective(chapter);
    }

    @Override
    public boolean exist(Integer bookUrl, Integer chapterUrl) {
        Chapter chapter = new Chapter();
        chapter.setBookUrl(bookUrl);
        chapter.setUrl(chapterUrl);

        return chapterMapper.selectCount(chapter) >= 1;
    }

    @Override
    public Chapter findBookNewChapter(Integer bookUrl) {
        return chapterMapper.selectNewChapter(bookUrl);
    }
}

package com.kangyonggan.app.book.book.biz.service;

import com.kangyonggan.app.book.book.model.vo.Book;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
public interface BookService {

    /**
     * 开始爬去书籍
     */
    void execute();

    /**
     * 判断书籍是否已经存在
     *
     * @param bookUrl
     * @return
     */
    boolean exist(String bookUrl);

    /**
     * 保存书籍
     *
     * @param book
     */
    void saveBook(Book book);

}

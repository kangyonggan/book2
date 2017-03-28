package com.kangyonggan.app.book.book.biz.service;

import com.kangyonggan.app.book.book.model.vo.Book;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
public interface BookService {

    /**
     * 判断书籍是否已经存在
     *
     * @param bookUrl
     * @return
     */
    boolean exist(Integer bookUrl);

    /**
     * 保存书籍
     *
     * @param book
     */
    void saveBook(Book book);

    /**
     * 分页查找书籍
     *
     * @param pageNum
     * @return
     */
    List<Book> findBooksByPage(int pageNum);

    /**
     * 更新书籍
     *
     * @param book
     */
    void updateBook(Book book);
}

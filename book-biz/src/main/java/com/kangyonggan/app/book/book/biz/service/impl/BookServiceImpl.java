package com.kangyonggan.app.book.book.biz.service.impl;

import com.kangyonggan.app.book.book.biz.service.BookService;
import com.kangyonggan.app.book.book.mapper.BookMapper;
import com.kangyonggan.app.book.book.model.vo.Book;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
@Service
@Log4j2
public class BookServiceImpl extends BaseService<Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public boolean exist(String bookUrl) {
        Book book = new Book();
        book.setUrl(bookUrl);

        return bookMapper.selectCount(book) >= 1;
    }

    @Override
    public void saveBook(Book book) {
        super.insertSelective(book);
    }


}

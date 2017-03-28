package com.kangyonggan.app.book.book.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.kangyonggan.app.book.book.biz.service.BookService;
import com.kangyonggan.app.book.book.mapper.BookMapper;
import com.kangyonggan.app.book.book.model.constants.AppConstants;
import com.kangyonggan.app.book.book.model.vo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
@Service
@Transactional
public class BookServiceImpl extends BaseService<Book> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public boolean exist(Integer bookUrl) {
        Book book = new Book();
        book.setUrl(bookUrl);

        return bookMapper.selectCount(book) >= 1;
    }

    @Override
    public void saveBook(Book book) {
        super.insertSelective(book);
    }

    @Override
    public List<Book> findBooksByPage(int pageNum) {
        PageHelper.startPage(pageNum, AppConstants.PAGE_SIZE);
        return bookMapper.selectBooks();
    }

    @Override
    public void updateBook(Book book) {
        super.updateByPrimaryKeySelective(book);
    }


}

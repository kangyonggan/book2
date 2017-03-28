package com.kangyonggan.app.book.book.biz;

import com.kangyonggan.app.book.book.biz.service.BookService;
import com.kangyonggan.app.book.book.model.vo.Book;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author kangyonggan
 * @since 2017/3/28
 */
public class BookServiceTest extends AbstractServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void testFindBooksByPage() {
        List<Book> books = bookService.findBooksByPage(1);
        Assert.assertTrue(!books.isEmpty());
    }

}

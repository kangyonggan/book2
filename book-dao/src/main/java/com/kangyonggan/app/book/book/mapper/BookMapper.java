package com.kangyonggan.app.book.book.mapper;

import com.kangyonggan.app.book.book.model.vo.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper extends MyMapper<Book> {

    /**
     * 查找书籍
     *
     * @return
     */
    List<Book> selectBooks();

}
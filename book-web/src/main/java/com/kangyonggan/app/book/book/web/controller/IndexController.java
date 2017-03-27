package com.kangyonggan.app.book.book.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author kangyonggan
 * @since 2017/3/27
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String layout() {
        return "layout";
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return getPathRoot();
    }

}

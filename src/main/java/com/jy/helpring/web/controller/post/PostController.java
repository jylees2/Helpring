package com.jy.helpring.web.controller.post;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/community")
public class PostController {

    /* 글 전체 조회 (카테고리별) */
    @GetMapping("/{postCategory_name}/post/page={page_no}&orderby={order_criteria}")
    public String readAll(){

        return "/community/post-all.html";
    }
}

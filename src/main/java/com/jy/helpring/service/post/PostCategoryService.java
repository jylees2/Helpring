package com.jy.helpring.service.post;

import com.jy.helpring.web.dto.post.PostCategoryDto;

import java.util.List;


public interface PostCategoryService {

    public List<PostCategoryDto.ResponseDto> findList();
}

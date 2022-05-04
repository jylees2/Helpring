package com.jy.helpring.service.category;

import com.jy.helpring.web.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    /** 카테고리 리스트 반환 **/
    List<CategoryDto.ResponseDto> findList();

}

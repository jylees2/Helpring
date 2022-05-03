package com.jy.helpring.service.category;

import com.jy.helpring.web.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto.ResponseDto> findList();
}

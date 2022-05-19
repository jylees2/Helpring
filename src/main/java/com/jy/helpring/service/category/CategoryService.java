package com.jy.helpring.service.category;

import com.jy.helpring.web.dto.category.CategoryDto;
import com.jy.helpring.web.dto.category.LectureCategoryDto;

import java.util.List;

public interface CategoryService {

    /** 카테고리 리스트 반환 **/
    List<CategoryDto.ResponseDto> findList();

    /** 카테고리 뷰 이름 반환 **/
    String getViewName(String name);

    /** 카테고리 리스트 반환 **/
    List<LectureCategoryDto.ResponseDto> findLectureList();

    /** 카테고리 뷰 이름 반환 **/
    String getLectureViewName(String name);
}

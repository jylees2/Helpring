package com.jy.helpring.service.category;

import com.jy.helpring.web.dto.category.LectureCategoryDto;
import com.jy.helpring.web.dto.category.PostCategoryDto;

import java.util.List;

/** 게시물 & 강의 카테고리 서비스 담당 **/
public interface CategoryService {

    /** 게시물 카테고리 리스트 반환 **/
    List<PostCategoryDto.ResponseDto> findPostList();

    /** 게시물 카테고리 뷰 이름 반환 **/
    String getPostViewName(String name);

    /** 강의 카테고리 리스트 반환 **/
    List<LectureCategoryDto.ResponseDto> findLectureList();

    /** 강의 카테고리 뷰 이름 반환 **/
    String getLectureViewName(String name);
}

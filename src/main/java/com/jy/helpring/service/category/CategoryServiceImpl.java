package com.jy.helpring.service.category;

import com.jy.helpring.domain.category.*;
import com.jy.helpring.web.dto.category.LectureCategoryDto;
import com.jy.helpring.web.dto.category.PostCategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final LectureCategoryRepository lectureCategoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    /** 게시물 카테고리 리스트 반환 **/
    @Override
    public List<PostCategoryDto.ResponseDto> findPostList() {

        List<PostCategory> categoryList = postCategoryRepository.findAll();

        /* 반환한 Category List 컬렉션 객체를 PostCategoryDto.ResponseDto List 컬렉션 객체로 변환 */
        return categoryList.stream().map(PostCategoryDto.ResponseDto::new).collect(Collectors.toList());

    }

    /** 게시물 카테고리 뷰 이름 반환 **/
    @Override
    public String getPostViewName(String name) {
        PostCategory postCategory = postCategoryRepository.findByName(name);
        return postCategory.getViewName();
    }

    /** 강의 카테고리 리스트 반환 **/
    @Override
    public List<LectureCategoryDto.ResponseDto> findLectureList() {

        List<LectureCategory> categoryList = lectureCategoryRepository.findAll();

        /* 반환한 Category List 컬렉션 객체를 CategoryDto.ResponseDto List 컬렉션 객체로 변환 */
        return categoryList.stream().map(LectureCategoryDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** 강의 카테고리 뷰 이름 반환 **/
    @Override
    public String getLectureViewName(String name) {
        LectureCategory category = lectureCategoryRepository.findByName(name);
        return category.getViewName();
    }
}

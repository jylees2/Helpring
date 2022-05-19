package com.jy.helpring.service.category;

import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.CategoryRepository;
import com.jy.helpring.domain.category.LectureCategory;
import com.jy.helpring.domain.category.LectureCategoryRepository;
import com.jy.helpring.web.dto.category.CategoryDto;
import com.jy.helpring.web.dto.category.LectureCategoryDto;
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

    private final CategoryRepository categoryRepository;
    private final LectureCategoryRepository lectureCategoryRepository;

    /** 게시물 카테고리 리스트 반환 **/
    @Override
    public List<CategoryDto.ResponseDto> findList() {

        List<Category> categoryList = categoryRepository.findAll();

        /* 반환한 Category List 컬렉션 객체를 CategoryDto.ResponseDto List 컬렉션 객체로 변환 */
        return categoryList.stream().map(CategoryDto.ResponseDto::new).collect(Collectors.toList());

    }

    /** 게시물 카테고리 뷰 이름 반환 **/
    @Override
    public String getViewName(String name) {
        Category category = categoryRepository.findByName(name);
        return category.getViewName();
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

package com.jy.helpring.service.category;

import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.CategoryRepository;
import com.jy.helpring.web.dto.category.CategoryDto;
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

    /** 카테고리 리스트 반환 **/
    @Override
    public List<CategoryDto.ResponseDto> findList() {

        List<Category> categoryList = categoryRepository.findAll();

        /* 반환한 Category List 컬렉션 객체를 CategoryDto.ResponseDto List 컬렉션 객체로 변환 */
        return categoryList.stream().map(CategoryDto.ResponseDto::new).collect(Collectors.toList());

    }

    /** 카테고리 뷰 이름 반환 **/
    @Override
    public String getViewName(String name) {
        Category category = categoryRepository.findByName(name);
        return category.getViewName();
    }
}

package com.jy.helpring.service.post;

import com.jy.helpring.domain.post.PostCategory;
import com.jy.helpring.domain.post.PostCategoryRepository;
import com.jy.helpring.web.dto.post.PostCategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService{

    private final PostCategoryRepository postCategoryRepository;

    @Override
    public List<PostCategoryDto.ResponseDto> findList() {
        List<PostCategory> postCategoryList = postCategoryRepository.findAll();

        /* 반환한 PostCategory List 컬렉션 객체를 PostCategoryDto.ResponseDto List 컬렉션 객체로 변환 */
        return postCategoryList.stream().map(PostCategoryDto.ResponseDto::new).collect(Collectors.toList());
    }
}

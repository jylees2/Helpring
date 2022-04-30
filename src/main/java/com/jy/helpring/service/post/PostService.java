package com.jy.helpring.service.post;

import com.jy.helpring.web.dto.post.PostDto;
import com.jy.helpring.web.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {


    /** 게시물 리스트 페이징 **/
    Page<PostDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, String category_name, String order_criteria);

    /** 페이징 정보 반환 **/
    PageVo getPageInfo(Page<PostDto.ResponsePageDto> postPageList, int pageNo);

    /** 게시물 검색 리스트 페이징 **/
    Page<PostDto.ResponsePageDto> searchPageList(Pageable pageable, int pageNo, String keyword, String category_name, String order_criteria);

    /** post_id 에 해당하는 게시물 반환 **/
    PostDto.ResponseDto findById(Long post_id);

    /** member_id 에 해당하는 게시물 리스트 페이징 **/
    Page<PostDto.ResponsePageDto> getMyPostPageList(Pageable pageable, int pageNo, Long member_id, String category_name);
}

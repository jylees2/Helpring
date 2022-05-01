package com.jy.helpring.service.post;

import com.jy.helpring.web.dto.post.PostDto;
import com.jy.helpring.web.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

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

    /** 게시물 저장 **/
    Long save(PostDto.RequestDto requestDto, Long member_id) throws IOException;

    /** 게시물 수정 **/
    void update(PostDto.RequestDto requestDto, Long member_id, Long post_id);

    /** 게시물 삭제 **/
    void delete(Long post_id);

    /** 글 좋아요 **/
    boolean saveLike(Long post_id, Long member_id);

    /** 글 좋아요 확인 **/
    boolean findLike(Long post_id, Long member_id);

    /** 글 조회수 업데이트 **/
    void updateView(Long post_id);
}

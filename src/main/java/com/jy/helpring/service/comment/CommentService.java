package com.jy.helpring.service.comment;

import com.jy.helpring.web.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    /** 댓글 작성 **/
    Long save(Long post_id, Long member_id, CommentDto.RequestDto requestDto);

    /** post_id에 대한 댓글 목록 조회 **/
    List<CommentDto.ResponseDto> findAllByPost(Long post_id);

    /** 댓글 수정 **/
    void update(Long comment_id, CommentDto.RequestDto requestDto);

    /** 댓글 삭제 **/
    void delete(Long comment_id);
}
package com.jy.helpring.service.comment;

import com.jy.helpring.web.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {

    /** post_id에 대한 댓글 리스트 조회 **/
    List<CommentDto.ResponseDto> findListById(Long post_id);
}

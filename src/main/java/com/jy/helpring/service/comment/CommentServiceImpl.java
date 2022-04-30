package com.jy.helpring.service.comment;

import com.jy.helpring.domain.comment.Comment;
import com.jy.helpring.domain.comment.CommentRepository;
import com.jy.helpring.domain.post.Post;
import com.jy.helpring.domain.post.PostRepository;
import com.jy.helpring.web.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public List<CommentDto.ResponseDto> findListById(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                                        new IllegalArgumentException("댓글 조회 실패 : 해당 게시물이 존재하지 않습니다."));

        /* 반환한 post의 댓글을 List 컬렉션으로 반환 */
        List<Comment> comment = post.getComment();

        /* 반환한 Comment List 컬렉션 객체를 CommentDto.ResponseDto List 컬렉션 객체로 변환 */
        return comment.stream().map(CommentDto.ResponseDto::new).collect(Collectors.toList());
    }
}

package com.jy.helpring.service.comment;

import com.jy.helpring.domain.comment.Comment;
import com.jy.helpring.domain.comment.CommentRepository;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.domain.post.Post;
import com.jy.helpring.domain.post.PostRepository;
import com.jy.helpring.web.dto.comment.CommentDto;
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
public class CommentServiceImpl implements CommentService{

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /** post_id에 대한 댓글 목록 조회 **/
    @Override
    public List<CommentDto.ResponseDto> findAllByPost(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                                        new IllegalArgumentException("댓글 조회 실패 : 해당 게시물이 존재하지 않습니다."));

        /* 반환한 post의 댓글을 List 컬렉션으로 반환 */
        List<Comment> comment = post.getComment();

        /* 반환한 Comment List 컬렉션 객체를 CommentDto.ResponseDto List 컬렉션 객체로 변환 */
        return comment.stream().map(CommentDto.ResponseDto::new).collect(Collectors.toList());
    }

    /** 댓글 작성 **/
    @Override
    public Long save(Long post_id, Long member_id, CommentDto.RequestDto requestDto) {

        /* member, post 정보 반환 */
        Member member = memberRepository.findById(member_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Post post = postRepository.findById(post_id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        /* 요청 받은 CommentDto에 member, post 정보 추가하여 entity로 변환 */
        Comment comment = requestDto.toEntity(member, post);
        commentRepository.save(comment);

        log.info("댓글 저장 완료");
        return comment.getId();
    }

    /** 댓글 수정 **/
    @Override
    public void update(Long comment_id, CommentDto.RequestDto requestDto) {

        Comment comment = commentRepository.findById(comment_id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        /* 요청 받은 수정 내용을 Comment 도메인 클래스의 update 메서드를 이용해 수정 */
        comment.update(requestDto.getContent());
        log.info("댓글 수정 완료");
    }

    /** 댓글 삭제 **/
    @Override
    public void delete(Long comment_id) {
        Comment comment = commentRepository.findById(comment_id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
        log.info("댓글 삭제 완료");
    }
}

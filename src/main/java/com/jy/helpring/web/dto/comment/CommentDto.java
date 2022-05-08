package com.jy.helpring.web.dto.comment;

import com.jy.helpring.domain.comment.Comment;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Request, Response Dto inner class 로 한 번에 관리
 */
public class CommentDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;
        private String content;
        private String createdDate, updatedDate;

        private Post post;
        private Member member;

        public void setPost(Post post){
            this.post = post;
        }

        public void setMember(Member member){
            this.member = member;
        }

        /* Dto -> Entity */
        public Comment toEntity(Member member, Post post) {
            Comment comment = Comment.builder()
                    .id(id)
                    .content(content)
                    .member(member)
                    .post(post)
                    .build();

            return comment;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long id;
        private String content;
        private String createdDate, updatedDate;
        private String writer;

        private Long memberId;
        private Long postId;

        /* Entity -> Dto */
        public ResponseDto(Comment comment){
            this.id = comment.getId();
            this.content = comment.getContent();
            this.writer = comment.getMember().getNickname();
            this.createdDate = comment.getCreatedDate();
            this.updatedDate = comment.getUpdatedDate();

            this.postId = comment.getPost().getId();
            this.memberId = comment.getMember().getId();
        }

    }
}

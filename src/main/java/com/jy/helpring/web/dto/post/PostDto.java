package com.jy.helpring.web.dto.post;

//import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.PostCategory;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.post.Post;
import com.jy.helpring.web.dto.comment.CommentDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Request, Response Dto inner class 로 한 번에 관리
 */
public class PostDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    public static class RequestDto{
        private Long id;
        private String title;
        private String content;
        private String fileName;
        private MultipartFile file;
        private int viewCount;
        private int likeCount;
//        private Member member;
//        private Category category;

        private Long member_id;
        private Long category_id;

        private String category_name;
        /* Dto -> Entity */
        public Post toEntity(Member member, PostCategory category){
            Post post = Post.builder()
                    .id(id)
                    .title(title)
                    .content(content)
                    .fileName(fileName)
                    .viewCount(0)
                    .likeCount(0)
                    .member(member)
                    .category(category)
                    .build();
            return post;
        }

//        /* Member 정보를 postDto에 담음 */
//        public void setMember(Member member){
//            this.member = member;
//        }


        /* 서버가 관리하는 파일명 추가 */
        public void addFileName(String storeFileName){
            this.fileName = storeFileName;
        };
    }

    /**
     * 게시물 정보를 리턴할 응답 클래스
     * 엔티티 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지
     */

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{
        private Long id;
        private Long member_id;
        private String title;
        private String content;
        private String writer;
        private String fileName;
        private int viewCount;
        private int likeCount;
        private String createdDate, updatedDate;
        private List<CommentDto.ResponseDto> comment;

        private String categoryViewName;

        /* Entity -> Dto */
        public ResponseDto(Post post){
            this.id = post.getId();
            this.member_id = post.getMember().getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.writer = post.getMember().getNickname();
            this.fileName = post.getFileName();
            this.viewCount = post.getViewCount();
            this.likeCount = post.getLikeCount();
            this.createdDate = post.getCreatedDate();
            this.updatedDate = post.getUpdatedDate();
            this.comment = post.getComment().stream().map(CommentDto.ResponseDto::new).collect(Collectors.toList());

            this.categoryViewName = post.getCategory().getViewName();
        }
    }

    /* 페이징 객체 */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponsePageDto{
        private Long id;
        private Long member_id;
        private String title;
        private String writer;
        private int viewCount;
        private int likeCount;
        private String createdDate;

        private String categoryViewName;
        private String categoryName;

        /* Entity -> Dto */
        public ResponsePageDto(Post post){
            this.id = post.getId();
            this.member_id = post.getMember().getId();
            this.title = post.getTitle();
            this.writer = post.getMember().getNickname();
            this.viewCount = post.getViewCount();
            this.likeCount = post.getLikeCount();
            this.createdDate = post.getCreatedDate();

            this.categoryViewName = post.getCategory().getViewName();
            this.categoryName = post.getCategory().getName();
        }
    }


}

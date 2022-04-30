package com.jy.helpring.web.dto.post;

import com.jy.helpring.domain.post.PostCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostCategoryDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;
        private String name;

        /* Dto -> Entity */
        public PostCategory toEntity(){
            PostCategory postCategory = PostCategory.builder()
                        .id(id)
                        .name(name)
                        .build();
            return postCategory;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;
        private String name;

        /* Entity -> Dto */
        public ResponseDto(PostCategory postCategory){
            this.id = postCategory.getId();
            this.name = postCategory.getName();
        }
    }
}

package com.jy.helpring.web.dto.category;

import com.jy.helpring.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;
        private String name;

        /* Dto -> Entity */
        public Category toEntity(){
            Category category = Category.builder()
                    .id(id)
                    .name(name)
                    .build();
            return category;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;
        private String name;

        /* Entity -> Dto */
        public ResponseDto(Category category){
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}

package com.jy.helpring.web.dto.category;

import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.LectureCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LectureCategoryDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;
        private String name;

        private String viewName;

        /* Dto -> Entity */
        public LectureCategory toEntity(){
            LectureCategory category = LectureCategory.builder()
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

        private String viewName;

        /* Entity -> Dto */
        public ResponseDto(LectureCategory category){
            this.id = category.getId();
            this.name = category.getName();
            this.viewName = category.getViewName();
        }
    }
}

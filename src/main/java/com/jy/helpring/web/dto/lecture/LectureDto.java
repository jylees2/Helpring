package com.jy.helpring.web.dto.lecture;

import com.jy.helpring.domain.category.LectureCategory;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.web.dto.review.ReviewDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public class LectureDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private String tutor;
        private String title;
        private String intro;
        private String content;
        private String fileName;
        private int price;
        private MultipartFile file;

        private Long category_id;

        /* Dto -> Entity */
        public Lecture toEntity(LectureCategory category){
            Lecture lecture = Lecture.builder()
                    .tutor(tutor)
                    .title(title)
                    .intro(intro)
                    .content(content)
                    .fileName(fileName)
                    .price(price)
                    .category(category)
                    .build();

            return lecture;
        }

        /** 서버가 관리하는 파일명 추가 **/
        public void addFileName(String storeFileName){
            this.fileName = storeFileName;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;
        private String tutor;
        private String title;
        private String intro;
        private String content;
        private String fileName;
        private int price;

        private String categoryViewName;
        private List<ReviewDto.ResponseDto> review;

        private String categoryName;
        /** Entity -> Dto **/
        public ResponseDto(Lecture lecture){
            this.id = lecture.getId();
            this.tutor = lecture.getTutor();
            this.title = lecture.getTitle();
            this.intro = lecture.getIntro();
            this.content = lecture.getContent();
            this.fileName = lecture.getFileName();
            this.price = lecture.getPrice();

            this.review = lecture.getReview().stream().map(ReviewDto.ResponseDto::new).collect(Collectors.toList());
            this.categoryName = lecture.getCategory().getName();
            this.categoryViewName = lecture.getCategory().getViewName();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponsePageDto{

        private Long id;
        private String title;
        private String intro;
        private int price;
        private String fileName;

        private String categoryName;
        private String categoryViewName;

        /** Entity -> Dto **/
        public ResponsePageDto(Lecture lecture){
            this.id = lecture.getId();
            this.title = lecture.getTitle();
            this.intro = lecture.getIntro();
            this.price = lecture.getPrice();
            this.fileName = lecture.getFileName();
            this.categoryName = lecture.getCategory().getName();
            this.categoryViewName = lecture.getCategory().getViewName();
        }
    }
}

package com.jy.helpring.web.dto.review;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{
        private Long id;
        private int rate;
        private String content;

        private Long member_id;
        private Long lecture_id;

        /** Dto -> Entity **/
        public Review toEntity(Member member, Lecture lecture){
            Review review = Review.builder()
                        .id(id)
                        .rate(rate)
                        .content(content)
                        .member(member)
                        .lecture(lecture)
                        .build();

            return review;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;

        private Long member_id;
        private int rate;
        private String content;
        private String createdDate, updatedDate;

        private String reviewWriter;

        /** Entity -> Dto **/
        public ResponseDto(Review review){
            this.id = review.getId();
            this.member_id = review.getMember().getId();
            this.rate = review.getRate();
            this.content = review.getContent();
            this.createdDate = review.getCreatedDate();
            this.updatedDate = review.getUpdatedDate();
            this.reviewWriter = review.getMember().getNickname();
        }
    }
}

package com.jy.helpring.web.dto.lecture;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.MemberWishLecture;
import com.jy.helpring.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberWishLectureDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequestDto{

        private Long id;
        private Long member_id;
        private Long lecture_id;

        /** Dto -> Entity **/
        public MemberWishLecture toEntity(Member member, Lecture lecture) {
            MemberWishLecture memberWishLecture = MemberWishLecture.builder()
                    .id(id)
                    .member(member)
                    .lecture(lecture)
                    .build();

            return memberWishLecture;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDto{

        private Long id;
        private Long member_id;
        private Long lecture_id;

        /* 찜 목록에서 보여줄 것 */
        private String lecture_title;
        private int lecture_price;
        private String lecture_fileName;
        private String category_name;


        /** Entity -> Dto **/
        public ResponseDto(MemberWishLecture memberWishLecture){
            this.id = memberWishLecture.getId();
            this.member_id = memberWishLecture.getId();
            this.lecture_id = memberWishLecture.getLecture().getId();

            this.lecture_title = memberWishLecture.getLecture().getTitle();
            this.lecture_price = memberWishLecture.getLecture().getPrice();
            this.lecture_fileName = memberWishLecture.getLecture().getFileName();
            this.category_name = memberWishLecture.getLecture().getCategory().getName();
        }
    }
}

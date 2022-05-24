package com.jy.helpring.web.dto.mylecture;

import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.MyLecture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MyLectureDto {


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponsePageDto{

        private Long id;
        private Long lectureId;
        private String lectureTitle;
        private String lectureIntro;
        private String fileName;
        private String categoryName;
        private String categoryViewName;

        /** Entity -> Dto **/
        public ResponsePageDto(MyLecture myLecture){
            this.id = myLecture.getId();
            this.lectureId = myLecture.getLecture().getId();
            this.lectureTitle = myLecture.getLecture().getTitle();
            this.lectureIntro = myLecture.getLecture().getIntro();
            this.fileName = myLecture.getLecture().getFileName();
            this.categoryName = myLecture.getLecture().getCategory().getName();
            this.categoryViewName = myLecture.getLecture().getCategory().getViewName();
        }
    }
}

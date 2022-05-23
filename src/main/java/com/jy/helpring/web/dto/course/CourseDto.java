package com.jy.helpring.web.dto.course;

import com.jy.helpring.domain.course.Course;
import com.jy.helpring.domain.lecture.Lecture;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class CourseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    public static class RequestDto{
        private Long id;
        private String title;
        private String fileName;
        private String section;
        private String content;
        private MultipartFile file;

        private Long lecture_id;

        /* Dto -> Entity */
        public Course toEntity(Lecture lecture){
            Course course = Course.builder()
                    .id(id)
                    .title(title)
                    .fileName(fileName)
                    .section(section)
                    .content(content)
                    .lecture(lecture)
                    .build();

            return course;
        }

        /** 서버가 관리하는 파일명 추가 **/
        public void addFileName(String storeFileName){
            this.fileName = storeFileName;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto{

        private Long id;
        private String title;
        private String fileName;
        private String section;
        private String content;

        private Long lecture_id;
        private String lecture_tutor;
        private String lecture_title;
        private String category_name;
        private String categoryViewName;

        /** Entity -> Dto **/
        public ResponseDto(Course course){
            this.id = course.getId();
            this.title = course.getTitle();
            this.fileName = course.getFileName();
            this.section = course.getSection();
            this.content = course.getContent();

            this.lecture_id = course.getLecture().getId();
            this.lecture_tutor = course.getLecture().getTutor();
            this.lecture_title = course.getLecture().getTitle();

            this.category_name = course.getLecture().getCategory().getName();
            this.categoryViewName = course.getLecture().getCategory().getViewName();
        }
    }
}

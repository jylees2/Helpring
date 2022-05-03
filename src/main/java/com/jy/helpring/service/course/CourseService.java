package com.jy.helpring.service.course;

import com.jy.helpring.domain.course.Course;
import com.jy.helpring.web.dto.course.CourseDto;

import java.util.List;

public interface CourseService {

    /** 재생할 강의 상세 정보 반환 **/
    CourseDto.ResponseDto getById(Long course_id);

    /** 강의 리스트 반환 **/
    List<CourseDto.ResponseDto> getAllById(Long lecture_id);
}

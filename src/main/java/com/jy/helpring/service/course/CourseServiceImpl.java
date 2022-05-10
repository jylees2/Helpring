package com.jy.helpring.service.course;

import com.jy.helpring.domain.course.Course;
import com.jy.helpring.domain.course.CourseRepository;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.web.dto.course.CourseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService{

    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;

    /** 재생할 강의 상세 정보 반환 **/
    @Override
    public CourseDto.ResponseDto getById(Long course_id) {
        Course course = courseRepository.findById(course_id).orElseThrow(() ->
                                            new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        return new CourseDto.ResponseDto(course);
    }

    /** 강의 리스트 반환 **/
    @Override
    public List<CourseDto.ResponseDto> getAllById(Long lecture_id) {
        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        List<Course> courseList = lecture.getCourse();

        return courseList.stream().map(CourseDto.ResponseDto::new).collect(Collectors.toList());
    }
}

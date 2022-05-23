package com.jy.helpring.service.course;

import com.jy.helpring.domain.category.LectureCategory;
import com.jy.helpring.domain.course.Course;
import com.jy.helpring.domain.course.CourseRepository;
import com.jy.helpring.domain.file.UploadFile;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.service.file.FileStore;
import com.jy.helpring.web.dto.course.CourseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService{

    /** 파일 저장 처리 객체 **/
    private final FileStore fileStore;
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

    /** ====================== 관리자 권한 ====================== **/

    /** 강의 영상 저장 **/
    @Override
    public Long save(CourseDto.RequestDto requestDto) throws IOException {
        /* 파일 저장 */
        MultipartFile course_file = requestDto.getFile();
        UploadFile uploadFile = fileStore.storeCourseFile(course_file);

        /* 파일명 추가 */
        requestDto.addFileName(uploadFile.getStoreFileName());

        /* lecture 정보 가져오기 */
        Long lecture_id = requestDto.getLecture_id();
        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        Course course = requestDto.toEntity(lecture);
        Course save = courseRepository.save(course);

        return save.getId();
    }
}

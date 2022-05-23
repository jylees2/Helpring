package com.jy.helpring.web.controller.admin;

import com.jy.helpring.service.category.CategoryService;
import com.jy.helpring.service.course.CourseService;
import com.jy.helpring.service.lecture.LectureService;
import com.jy.helpring.web.dto.category.LectureCategoryDto;
import com.jy.helpring.web.dto.course.CourseDto;
import com.jy.helpring.web.dto.lecture.LectureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final CategoryService categoryService;
    private final LectureService lectureService;
    private final CourseService courseService;

    /** =================== 강의 =================== **/

    /** 강의 저장 폼 반환 **/
    @GetMapping("/lecture/saveForm")
    public String saveLecture(Model model){

        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();

        model.addAttribute("categoryList", categoryList);

        return "lecture/lecture-save";
    }

    /** 강의 저장 **/
    @PostMapping("/lecture/save")
    public String saveLecture(@ModelAttribute LectureDto.RequestDto requestDto) throws IOException {
        lectureService.save(requestDto);

        return "redirect:/lecture/";
    }

//    /** 강의 영상 저장 폼 **/
//    @GetMapping("/course/saveForm")
//    public String saveCourse(Model model){
//
//        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();
//
//        model.addAttribute("categoryList", categoryList);
//
//        return "lecture/course-save";
//    }

    /** 강의 영상 저장 **/
    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute CourseDto.RequestDto requestDto) throws IOException {
        courseService.save(requestDto);

        return "redirect:/lecture/";
    }

}

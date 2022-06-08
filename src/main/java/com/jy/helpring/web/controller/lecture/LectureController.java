package com.jy.helpring.web.controller.lecture;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.domain.Role;
import com.jy.helpring.service.cart.CartService;
import com.jy.helpring.service.category.CategoryService;
import com.jy.helpring.service.course.CourseService;
import com.jy.helpring.service.lecture.LectureService;
import com.jy.helpring.service.lecture.MyLectureService;
import com.jy.helpring.service.review.ReviewService;
import com.jy.helpring.web.dto.category.LectureCategoryDto;
import com.jy.helpring.web.dto.course.CourseDto;
import com.jy.helpring.web.dto.lecture.LectureDto;
import com.jy.helpring.web.dto.mylecture.MyLectureDto;
import com.jy.helpring.web.dto.review.ReviewDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lecture")
@Slf4j
public class LectureController {

    private final LectureService lectureService;
    private final CourseService courseService;
    private final MyLectureService myLectureService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final CartService cartService;

    /** 강의 전체 조회 **/
    @GetMapping("/")
    public String readAllLecture(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                 Pageable pageable,
                                 @AuthenticationPrincipal UserAdapter user,
                                 Model model){

        /* 강의 카테고리 리스트 반환 */
        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();
        model.addAttribute("categoryList", categoryList);


        /** ========== 페이징 처리 ========== **/
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        Page<LectureDto.ResponsePageDto> lecturePageList =
                lectureService.getAllPageList(pageable, pageNo); // 페이지 객체 생성
        PageVo pageVo = lectureService.getPageInfo(lecturePageList, pageNo);

        model.addAttribute("lectureList", lecturePageList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageVo", pageVo);

        return "lecture/lecture-all";
    }

    /** 강의 조회 (카테고리별 & 페이징) **/
    /* /lecture/{category_name}/page={pageNo} */
    @GetMapping("/{category_name}")
    public String readLecture(@PathVariable(required = false) String category_name,
                          @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                          Pageable pageable,
                          @AuthenticationPrincipal UserAdapter user,
                          Model model){

        /* 강의 카테고리 리스트, 현재 카테고리 반환 */
        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("category_name", category_name);

        String lectureViewName = categoryService.getLectureViewName(category_name);
        model.addAttribute("category_viewName", lectureViewName);

        /** ========== 페이징 처리 ========== **/
        pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

        Page<LectureDto.ResponsePageDto> lecturePageList =
                lectureService.getPageList(pageable, pageNo, category_name); // 페이지 객체 생성
        PageVo pageVo = lectureService.getPageInfo(lecturePageList, pageNo);

        model.addAttribute("lectureList", lecturePageList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageVo", pageVo);

        return "lecture/lecture-category";
    }

    /** 강의 상세 조회 **/
    /* /lecture/{category_name}/{lecture_id} */
    @GetMapping("/{category_name}/{lecture_id}")
    public String readByLectureId(@PathVariable("category_name") String category_name,
                                  @PathVariable("lecture_id") Long lecture_id,
                                  @AuthenticationPrincipal UserAdapter user,
                                  Model model){

        /** 구매한 강의인지 확인 lectureCheck, 구매한 강의라면 true, 구매하지 않은 강의라면 false **/
        /** 리뷰한 강의인지 확인 reviewCheck **/

        boolean lectureCheck;
        boolean reviewCheck;

        if(user != null){
        	            
            // 로그인한 사용자라면
        	
        	Long member_id = user.getMemberDto().getId();
            model.addAttribute("member_id", member_id);

            // 관리자라면 무조건 lectureCheck = true
        	if(user.getMemberDto().getRole() == Role.ADMIN) {
        		lectureCheck = true;
        	} else {
        		lectureCheck = lectureService.myLectureCheck(member_id, lecture_id);
        	}

            reviewCheck = reviewService.reviewCheck(member_id, lecture_id);

        } else {
            // 로그인하지 않은 사용자도 false
            lectureCheck = false;
            reviewCheck = false;
        }

        model.addAttribute("lectureCheck", lectureCheck);
        model.addAttribute("reviewCheck", reviewCheck);

        /* 강의, 리뷰 목록 반환 */
        LectureDto.ResponseDto lecture = lectureService.getById(lecture_id);
        List<ReviewDto.ResponseDto> reviewList = reviewService.findAllByLecture(lecture_id);
        List<CourseDto.ResponseDto> courseList = courseService.getAllById(lecture_id);

        model.addAttribute("lecture", lecture);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("courseList", courseList);

        /* 강의 카테고리 리스트, 현재 카테고리 반환 */
        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("category_name", category_name);

        return "lecture/lecture-read";
    }

    /** 강의 리스트(영상) 상세 조회 (강의 수강 화면) **/

    /* /lecture/course/{course_id} */
    @GetMapping("/course/{course_id}")
    public String readByCourseId(@PathVariable Long course_id,
                                 Model model){

        /* 수강할 강의 수업 정보 */
        CourseDto.ResponseDto course = courseService.getById(course_id);

        /** 해당 강의의 수업 목록 반환 **/
        Long lecture_id = course.getLecture_id();
        List<CourseDto.ResponseDto> courseList = courseService.getAllById(lecture_id);

        model.addAttribute("course", course);
        model.addAttribute("courseList", courseList);

        return "lecture/lecture-course";
    }

    /** 강의 결제 성공 후 수강 권한 획득 **/
    @PostMapping("/payment")
    public String payment(@AuthenticationPrincipal UserAdapter user,
                          Model model){

        Long member_id = user.getMemberDto().getId();

        /** 수강 권한 부여 **/
        myLectureService.getLecture(member_id);

        /** 장바구니에서 해당 강의 삭제 **/
        cartService.deleteAll(member_id);

        /* lecture/myClass 로 리다이렉트 */
        return "redirect:/myLecture";
    }
}

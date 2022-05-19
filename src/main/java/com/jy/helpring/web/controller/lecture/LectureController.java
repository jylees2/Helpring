package com.jy.helpring.web.controller.lecture;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.cart.CartService;
import com.jy.helpring.service.category.CategoryService;
import com.jy.helpring.service.course.CourseService;
import com.jy.helpring.service.lecture.LectureService;
import com.jy.helpring.service.lecture.MyLectureService;
import com.jy.helpring.service.review.ReviewService;
import com.jy.helpring.web.dto.category.CategoryDto;
import com.jy.helpring.web.dto.category.LectureCategoryDto;
import com.jy.helpring.web.dto.course.CourseDto;
import com.jy.helpring.web.dto.lecture.LectureDto;
import com.jy.helpring.web.dto.review.ReviewDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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

        /* 강의 카테고리 리스트 반환 */
        List<LectureCategoryDto.ResponseDto> categoryList = categoryService.findLectureList();
        model.addAttribute("categoryList", categoryList);

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
    /* /lecture/{lecture_id} */
    @GetMapping("/{lecture_id}")
    public String readByLectureId(@PathVariable Long lecture_id,
                                  @AuthenticationPrincipal UserAdapter user,
                                  Model model){

        Long member_id = user.getMemberDto().getId();

        /** 강의, 리뷰 리스트 반환 **/
        LectureDto.ResponseDto lecture = lectureService.getById(lecture_id);
        List<ReviewDto.ResponseDto> reviewList = reviewService.findAllByLecture(lecture_id);

        model.addAttribute("lecture", lecture);
        model.addAttribute("reviewList", reviewList);

        /** 구매한 강의인지 확인 myLectureCheck **/
        boolean lectureCheck;
        if(user == null){
            lectureCheck = false;
        } else {
            lectureCheck = lectureService.myLectureCheck(member_id, lecture_id);
        }
        model.addAttribute("lectureCheck", lectureCheck);

        /** 리뷰한 강의인지 확인 myReviewCheck **/
        boolean reviewCheck;
        if(user == null){
            reviewCheck = false;
        } else {
            reviewCheck = reviewService.reviewCheck(member_id, lecture_id);
        }
        model.addAttribute("reviewCheck", reviewCheck);

        return "lecture/lecture-read";
    }

    /** 강의 리스트 상세 조회 (강의 수강 화면) **/

    /* /lecture/course/{course_id} */
    @GetMapping("/course/{course_id}")
    public String readByCourseId(@PathVariable Long course_id,
                                 Model model){

        /** 수강할 강의 정보 **/
        CourseDto.ResponseDto course = courseService.getById(course_id);

        /** 강의 리스트 반환 **/
        Long lecture_id = course.getLecture_id();
        List<CourseDto.ResponseDto> courseList = courseService.getAllById(lecture_id);

        model.addAttribute("course", course);
        model.addAttribute("courseList", courseList);

        return "lecture/course";
    }

    /** 내가 수강 중인 강의 조회 **/
    /* /lecture/mylecture */
    @GetMapping("/myLecture")
    public String myLecture(@AuthenticationPrincipal UserAdapter user,
                            Model model){

        Long member_id = user.getMemberDto().getId();

        List<CourseDto.ResponseDto> courseList = courseService.getAllById(member_id);

        model.addAttribute("courseList", courseList);
        model.addAttribute("user", user);

        return "lecture/myLecture";
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
        return "redirect:/lecture/lecture";
    }
}

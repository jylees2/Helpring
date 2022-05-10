package com.jy.helpring.web.controller.review;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.review.ReviewService;
import com.jy.helpring.web.dto.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@Slf4j
public class ReviewRestController {

    private final ReviewService reviewService;

    /** 리뷰 업로드 **/
    /* /lecture/{lecture_id}/review */
    @PostMapping("/{lecture_id}/review")
    public ResponseEntity upload(@PathVariable Long lecture_id,
                                 @RequestBody ReviewDto.RequestDto requestDto,
                                 @AuthenticationPrincipal UserAdapter user){

        Long member_id = user.getMemberDto().getId();

        reviewService.save(member_id, lecture_id, requestDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    /** 리뷰 삭제 **/
    /* /lecture/{lecture_id}/review/{review_id} */
    @DeleteMapping("/{lecture_id}/review/{review_id}")
    public ResponseEntity delete(@PathVariable Long review_id){

        reviewService.delete(review_id);

        return new ResponseEntity(HttpStatus.OK);
    }
}

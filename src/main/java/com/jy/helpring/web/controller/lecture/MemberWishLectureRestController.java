package com.jy.helpring.web.controller.lecture;

import com.jy.helpring.config.auth.UserAdapter;
import com.jy.helpring.service.lecture.MemberWishLectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wish")
public class MemberWishLectureRestController {

    private final MemberWishLectureService memberWishLectureService;

    /** 위시리스트에 강의 추가 **/
    @PostMapping("/{lecture_id}")
    public boolean save(@PathVariable Long lecture_id,
                        @AuthenticationPrincipal UserAdapter user){

        Long member_id = user.getMemberDto().getId();

        if(memberWishLectureService.checkWish(member_id, lecture_id)){
            /* 해당 강의를 찜했다면 찜 목록에서 삭제 */
            memberWishLectureService.deleteById(member_id, lecture_id);
            return true;
        } else {
            /* 해당 강의를 찜하지 않았다면 */
            memberWishLectureService.save(member_id, lecture_id);
            return false;
        }
    }

    /** 위시리스트에서 강의 삭제 **/
    @DeleteMapping("/{wish_id")
    public ResponseEntity delete(@PathVariable Long wish_id){
        memberWishLectureService.deleteWish(wish_id);
        return new ResponseEntity(HttpStatus.OK);
    }
}

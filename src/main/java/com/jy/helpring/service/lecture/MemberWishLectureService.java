package com.jy.helpring.service.lecture;

import com.jy.helpring.web.dto.lecture.MemberWishLectureDto;

import java.util.List;

public interface MemberWishLectureService {

    /** 찜 리스트 반환 **/
    List<MemberWishLectureDto.ResponseDto> findByMemberId(Long member_id);

    /** member_id에 해당하는 찜 리스트 존재 여부 확인 - 유저의 찜 목록이 존재하는지 확인 **/
    boolean checkHaveWish(Long member_id);

    /** member_id가 lecture_id를 찜 리스트에 추가했는지 확인 - 유저가 특정 강의를 찜했는지 확인 **/
    boolean checkWish(Long member_id, Long lecture_id);

    /** 찜 목록에 강의 추가 **/
    void save(Long member_id, Long lecture_id);

    /** 강의 상세 페이지에서 member_id, lecture_id에 해당하는 찜 삭제 - 유저가 특정 강의 찜 취소 **/
    void deleteById(Long member_id, Long lecture_id);

    /** 찜 목록에서 wish_id에 해당하는 찜 삭제 - 유저가 특정 강의 찜 취소 **/
    void deleteWish(Long wish_id);
}

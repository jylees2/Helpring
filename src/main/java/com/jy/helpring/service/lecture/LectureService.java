package com.jy.helpring.service.lecture;

import com.jy.helpring.web.dto.lecture.LectureDto;
import com.jy.helpring.web.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface LectureService {

    /** 게시물 리스트 페이징 **/
    Page<LectureDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, String category_name);

    /** 페이징 정보 반환 **/
    PageVo getPageInfo(Page<LectureDto.ResponsePageDto> lecturePageList, int pageNo);

    /** lecture_id 에 해당하는 게시물 반환 **/
    LectureDto.ResponseDto getById(Long lecture_id);

    /** 현재 로그인한 유저가 구매한 강의인지 확인 **/
    boolean myLectureCheck(Long member_id, Long lecture_id);


    /** ====================== 관리자 권한 ====================== **/

    /** 강의 저장 **/
    Long save(LectureDto.RequestDto requestDto) throws IOException;
}

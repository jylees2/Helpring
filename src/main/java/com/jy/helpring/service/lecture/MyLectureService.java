package com.jy.helpring.service.lecture;

import com.jy.helpring.web.dto.lecture.LectureDto;
import com.jy.helpring.web.dto.mylecture.MyLectureDto;
import com.jy.helpring.web.vo.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MyLectureService {

    /** 구매한 강의에 대한 권한 부여 **/
    void getLecture(Long member_id);

    /** 유저가 구매한 강의 리스트가 존재하는지 확인 **/
    boolean checkLecture(Long member_id);
    /** 유저가 구매한 강의 리스트 반환 **/
    Page<MyLectureDto.ResponsePageDto> getAllPageList(Long member_id, Pageable pageable, int pageNo);

    /** 페이징 정보 반환 **/
    PageVo getPageInfo(Page<MyLectureDto.ResponsePageDto> myLecturePageList, int pageNo);



}

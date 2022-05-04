package com.jy.helpring.web.controller.lecture;

import com.jy.helpring.service.lecture.LectureService;
import com.jy.helpring.web.dto.lecture.LectureDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
public class LectureRestController {

    /** 관리자 권한 - 강의 저장 **/
    private final LectureService lectureService;

    @PostMapping("/")
    public ResponseEntity save(@ModelAttribute LectureDto.RequestDto requestDto) throws IOException {
        lectureService.save(requestDto);

        return new ResponseEntity(HttpStatus.OK);
    }
}

package com.jy.helpring.service.lecture;

import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.category.CategoryRepository;
import com.jy.helpring.domain.file.UploadFile;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.LectureRepository;
import com.jy.helpring.domain.lecture.MyLectureRepository;
import com.jy.helpring.domain.member.MemberRepository;
import com.jy.helpring.service.file.FileStore;
import com.jy.helpring.web.dto.lecture.LectureDto;
import com.jy.helpring.web.vo.PageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LectureServiceImpl implements LectureService{

    private static final int PAGE_LECTURE_COUNT = 10; // 한 화면에 보일 컨텐츠 수

    private final LectureRepository lectureRepository;
    private final MyLectureRepository myLectureRepository;
    private final CategoryRepository categoryRepository;

    /** 파일 저장 처리 객체 **/
    private final FileStore fileStore;

    /** 전체 강의 리스트 페이징 **/
    @Override
    public Page<LectureDto.ResponsePageDto> getAllPageList(Pageable pageable, int pageNo) {

        /* pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_LECTURE_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        /* category_name에 해당하는 post 페이지 객체 반환 */
        Page<Lecture> page = lectureRepository.findAll(pageable);

        /* Dto로 변환 */
        Page<LectureDto.ResponsePageDto> lecturePageList = page.map(
                lecture -> new LectureDto.ResponsePageDto(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getIntro(),
                        lecture.getPrice(),
                        lecture.getFileName())
        );

        return lecturePageList;
    }

    /** 카테고리별 강의 리스트 페이징 **/
    @Override
    public Page<LectureDto.ResponsePageDto> getPageList(Pageable pageable, int pageNo, String category_name) {

        /* pageable 객체 반환 */
        pageable = PageRequest.of(pageNo, PAGE_LECTURE_COUNT, Sort.by(Sort.Direction.DESC, "id"));

        /* category_name에 해당하는 post 페이지 객체 반환 */
        Page<Lecture> page = lectureRepository.findByCategory_Name(category_name, pageable);

        /* Dto로 변환 */
        Page<LectureDto.ResponsePageDto> lecturePageList = page.map(
                lecture -> new LectureDto.ResponsePageDto(
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getIntro(),
                        lecture.getPrice(),
                        lecture.getFileName())
        );

        return lecturePageList;
    }

    /** 페이징 정보 반환 **/
    @Override
    public PageVo getPageInfo(Page<LectureDto.ResponsePageDto> lecturePageList, int pageNo) {
        int totalPage = lecturePageList.getTotalPages();


        // 현재 페이지를 통해 현재 페이지 그룹의 시작 페이지를 구함
        int startNumber = (int)((Math.floor(pageNo/PAGE_LECTURE_COUNT)*PAGE_LECTURE_COUNT)+1 <= totalPage ? (Math.floor(pageNo/PAGE_LECTURE_COUNT)*PAGE_LECTURE_COUNT)+1 : totalPage);
        // 전체 페이지 수와 현재 페이지 그룹의 시작 페이지를 통해 현재 페이지 그룹의 마지막 페이지를 구함
        int endNumber = (startNumber + PAGE_LECTURE_COUNT-1 < totalPage ? startNumber + PAGE_LECTURE_COUNT-1 : totalPage);
        boolean hasPrev = lecturePageList.hasPrevious();
        boolean hasNext = lecturePageList.hasNext();

        return new PageVo(totalPage, startNumber, endNumber, hasPrev, hasNext);
    }

    /** lecture_id 로 lecture 객체를 찾아 lectureDto.ResponseDto로 반환 **/
    @Override
    public LectureDto.ResponseDto getById(Long lecture_id) {

        Lecture lecture = lectureRepository.findById(lecture_id).orElseThrow(() ->
                                    new IllegalArgumentException("해당 강의가 존재하지 않습니다."));

        return new LectureDto.ResponseDto(lecture);
    }

    /** 현재 로그인한 유저가 구매한 강의인지 확인 **/
    @Override
    public boolean myLectureCheck(Long member_id, Long lecture_id) {

        /** 로그인 유저가 구매한 강의라면 true, 구매하지 않은 강의라면 false **/
        if(myLectureRepository.findByMember_IdAndLecture_Id(member_id, lecture_id).isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    /** ====================== 관리자 권한 ====================== **/

    /** 강의 저장 **/
    @Override
    public Long save(LectureDto.RequestDto requestDto) throws IOException{

        /* 파일 저장 */
        MultipartFile lecture_file = requestDto.getFile();
        UploadFile uploadFile = fileStore.storeFile(lecture_file);

        /* 파일명 추가 */
        requestDto.addFileName(uploadFile.getStoreFileName());

        /* */
        Long category_id = requestDto.getCategory_id();
        Category category = categoryRepository.findById(category_id).orElseThrow(() ->
                new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        Lecture lecture = requestDto.toEntity(category);

        return lectureRepository.save(lecture).getId();
    }

}

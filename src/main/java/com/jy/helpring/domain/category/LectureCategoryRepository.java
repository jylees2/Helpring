package com.jy.helpring.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureCategoryRepository extends JpaRepository<LectureCategory, Long> {

    /** 강의 카테고리 리포지토리 **/

    /** 카테고리 영어 이름으로 카테고리 반환 **/
    LectureCategory findByName(String name);
}

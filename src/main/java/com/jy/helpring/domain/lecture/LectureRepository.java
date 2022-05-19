package com.jy.helpring.domain.lecture;

import com.jy.helpring.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    /** 카테고리 name 으로 lecture 찾기 **/
    Page<Lecture> findByCategory_Name(String category_name, Pageable pageable);
}

package com.jy.helpring.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    /* 카테고리 id 로 post 찾기 */
    Page<Post> findByPostCategory_Id(Long category_id, Pageable pageable);
}

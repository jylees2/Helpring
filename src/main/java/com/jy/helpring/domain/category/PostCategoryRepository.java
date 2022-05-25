package com.jy.helpring.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    /** 카테고리 영어 이름으로 카테고리 반환 **/
    PostCategory findByName(String name);
}

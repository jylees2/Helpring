package com.jy.helpring.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {


    /** 카테고리 name 으로 post 찾기 **/
    Page<Post> findByCategory_Name(String category_name, Pageable pageable);

    /** 카테고리 name 으로 keyword를 포함하고 있는 post 찾기 - 카테고리별 키워드 검색 **/
    Page<Post> findByCategory_NameAndTitleContaining(String category_name, String keyword, Pageable pageable);

    /** 카테고리 name 과 member id로 post 찾기 - **/
    Page<Post> findByCategory_NameAndMember_Id(String category_name, Long member_id, Pageable pageable);

    /** member_id로 post 찾기 - 유저 본인이 작성한 게시물 반환 **/
    Page<Post> findByMember_Id(Long member_id, Pageable pageable);

    /** 좋아요 추가 **/
    @Modifying
    @Query(value = "update Post post set post.likeCount = post.likeCount + 1 where post.id = :post_id")
    int plusLike(@Param("post_id") Long post_id);

    /** 좋아요 삭제 **/
    @Modifying
    @Query(value = "update Post post set post.likeCount = post.likeCount - 1 where post.id = :post_id")
    int minusLike(@Param("post_id") Long post_id);

    /** 조회수 업데이트 **/
    @Modifying
    @Query(value = "update Post post set post.viewCount = post.viewCount + 1 where post.id = :post_id")
    int updateView(@Param("post_id") Long post_id);
}

package com.jy.helpring.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLikePostRepository extends JpaRepository<MemberLikePost, Long> {

    /** 유저가 특정 게시물을 좋아요 했는지 확인 **/
    boolean existsByPost_IdAndMember_Id(Long post_id, Long member_id);

    /** 좋아요 삭제 **/
    void deleteByPost_IdAndMember_Id(Long post_id, Long member_id);
}

package com.jy.helpring.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLikePostRepository extends JpaRepository<MemberLikePost, Long> {

    /** 좋아요 한 게시물 반환 **/
    Optional<MemberLikePost> findByPost_IdAndMember_Id(Long post_id, Long member_id);

    /** 좋아요 삭제 **/
    void deleteByPost_IdAndMember_Id(Long post_id, Long member_id);
}

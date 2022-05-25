package com.jy.helpring.domain.member;

import com.jy.helpring.domain.BaseTimeEntity;
import com.jy.helpring.domain.Role;
import com.jy.helpring.domain.cart.Cart;
import com.jy.helpring.domain.lecture.Lecture;
import com.jy.helpring.domain.lecture.MemberWishLecture;
import com.jy.helpring.domain.lecture.MyLecture;
import com.jy.helpring.domain.post.MemberLikePost;
import com.jy.helpring.domain.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 로그인할 회원 아이디 */
    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 회원가입하면 무조건 USER로 설정할 것

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Post> post;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Lecture> lecture;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<Cart> cart;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<MemberLikePost> like;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<MemberWishLecture> wish;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id asc")
    private List<MyLecture> myLecture;

    /** 회원 정보 수정 메서드 **/
//
//    public void updateUsername(String username){
//        this.username = username;
//    }
//
//    public void updateNickname(String nickname){
//        this.nickname = nickname;
//    }

    public void update(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }

    /** 비밀번호 변경 메서드 **/
    public void updatePassword(String password){
        this.password = password;
    }

    /** 소셜 로그인 시 이미 등록된 회원인 경우 수정 날짜 업데이트, 기존 데이터는 보존 **/
    public Member updateUpdatedDate(){
        this.onPreUpdate(); // 수정 날짜 업데이트
        return this;
    }
}

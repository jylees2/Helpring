package com.jy.helpring.domain.lecture;

import com.jy.helpring.domain.BaseTimeEntity;
import com.jy.helpring.domain.category.Category;
import com.jy.helpring.domain.course.Course;
import com.jy.helpring.domain.member.Member;
import com.jy.helpring.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tutor;

    @Column(nullable = false, unique = true)
    private String title;

    /* 강의 소개 */
    @Column(nullable = false)
    private String intro;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /* 파일 경로명 */
    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> course;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> review;

}

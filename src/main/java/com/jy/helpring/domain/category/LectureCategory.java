package com.jy.helpring.domain.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** 강의 카테고리 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class LectureCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 카테고리 영어 이름

    @Column(nullable = false, unique = true)
    private String viewName; // 화면에 보여줄 한글 이름
}

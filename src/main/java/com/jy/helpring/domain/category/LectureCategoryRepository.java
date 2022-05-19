package com.jy.helpring.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureCategoryRepository extends JpaRepository<LectureCategory, Long> {
    LectureCategory findByName(String name);
}

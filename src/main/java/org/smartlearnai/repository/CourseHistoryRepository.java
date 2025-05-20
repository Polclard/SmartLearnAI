package org.smartlearnai.repository;

import org.smartlearnai.model.CourseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseHistoryRepository extends JpaRepository<CourseHistory, Long> {
    List<CourseHistory> findByUserId(Long userId);
    List<CourseHistory> findByCourseId(Long courseId);
    List<CourseHistory> findByUserIdAndCourseId(Long userId, Long courseId);
    List<CourseHistory> findByCourseTitleContainingIgnoreCase(String courseTitle);
    List<CourseHistory> findByUserIdAndIsFavoriteTrue(Long userId);
}
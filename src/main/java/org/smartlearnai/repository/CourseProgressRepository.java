package org.smartlearnai.repository;

import org.smartlearnai.model.Course;
import org.smartlearnai.model.CourseProgress;
import org.smartlearnai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {

    // Find all progress for a specific user
    List<CourseProgress> findByUser(User user);

    // Find all progress for a specific course
    List<CourseProgress> findByCourse(Course course);

    // Find progress by user and course
    CourseProgress findByUserAndCourse(User user, Course course);
}

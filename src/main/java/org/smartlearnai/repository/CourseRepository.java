package org.smartlearnai.repository;

import org.smartlearnai.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String query, String query1);
}

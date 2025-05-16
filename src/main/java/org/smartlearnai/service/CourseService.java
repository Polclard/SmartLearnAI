package org.smartlearnai.service;

import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.LessonDto;

import java.util.List;

public interface CourseService {
    CourseDto generateCourse(String title, String description, String promptUsed, List<LessonDto> lessons);
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
    CourseDto toggleFavorite(Long id);
    List<CourseDto> filterCourse(String query);
}
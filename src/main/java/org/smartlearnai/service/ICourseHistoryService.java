package org.smartlearnai.service;

import org.smartlearnai.model.CourseHistory;

import java.util.List;

public interface ICourseHistoryService {
    CourseHistory getCourseHistoryByUserId(Long userId);
    List<CourseHistory> getCourseHistoryByEmail(String email);
    CourseHistory saveCourseInCourseHistory(Long userId, Long courseId, String courseData);
}

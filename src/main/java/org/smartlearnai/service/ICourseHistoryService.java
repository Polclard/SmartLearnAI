package org.smartlearnai.service;

import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.model.Message;

import java.util.List;

public interface ICourseHistoryService {
    CourseHistory getCourseHistoryByUserId(Long userId);
    List<CourseHistory> getCourseHistoryByEmail(String email);
    CourseHistory saveCourseInCourseHistory(Long userId, Long courseId, List<Message> courseData);
}

package org.smartlearnai.service;
import org.smartlearnai.model.CourseProgress;
import org.smartlearnai.model.User;
import org.smartlearnai.model.dto.CourseProgressDto;

import java.util.List;

public interface ICourseProgress {
    CourseProgress saveCourseProgress(CourseProgressDto courseProgress);
    CourseProgress getCourseProgress(Long id);
    CourseProgress updateCourseProgress(CourseProgressDto courseProgress);

    List<CourseProgress> getAllCoursesProgressByUserId(Long userId);
    List<CourseProgress> getAllCoursesProgressByUser(User user);
    List<CourseProgress> getAllCoursesProgressByUserEmail(String email);
}

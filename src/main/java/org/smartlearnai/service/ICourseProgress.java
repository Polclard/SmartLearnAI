package org.smartlearnai.service;
import org.smartlearnai.model.CourseProgress;
import org.smartlearnai.model.dto.CourseProgressDto;

public interface ICourseProgress {
    CourseProgress saveCourseProgress(CourseProgressDto courseProgress);
    CourseProgress getCourseProgress(Long id);
    CourseProgress updateCourseProgress(CourseProgressDto courseProgress);
}

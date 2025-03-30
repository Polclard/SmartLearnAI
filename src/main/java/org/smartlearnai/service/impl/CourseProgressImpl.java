package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.CourseProgress;
import org.smartlearnai.model.dto.CourseProgressDto;
import org.smartlearnai.repository.CourseProgressRepository;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.ICourseProgress;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseProgressImpl implements ICourseProgress {

    private final CourseProgressRepository courseProgressRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public CourseProgress saveCourseProgress(CourseProgressDto courseProgress) {
        return courseProgressRepository.save(
                new CourseProgress(
                        userRepository.findById(courseProgress.getUserId()).orElseThrow(RuntimeException::new),
                        courseRepository.findById(courseProgress.getCourseId()).orElseThrow(RuntimeException::new),
                        courseProgress.getCompletedLessons(),
                        courseProgress.isQuizPassed()
                )
        );
    }

    @Override
    public CourseProgress getCourseProgress(Long id) {
        return courseProgressRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public CourseProgress updateCourseProgress(CourseProgressDto courseProgress) {
        CourseProgress foundCourseProgress = courseProgressRepository.findById(courseProgress.getId()).orElseThrow(RuntimeException::new);

        foundCourseProgress.setUser(userRepository.findById(courseProgress.getUserId()).orElseThrow(RuntimeException::new));
        foundCourseProgress.setCourse( courseRepository.findById(courseProgress.getCourseId()).orElseThrow(RuntimeException::new));
        foundCourseProgress.setCompletedLessons(courseProgress.getCompletedLessons());
        foundCourseProgress.setQuizPassed(courseProgress.isQuizPassed());

        return courseProgressRepository.save(foundCourseProgress);
    }
}

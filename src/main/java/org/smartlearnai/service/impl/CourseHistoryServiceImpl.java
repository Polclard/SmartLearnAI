package org.smartlearnai.service.impl;

import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.model.User;
import org.smartlearnai.repository.CourseHistoryRepository;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.ICourseHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseHistoryServiceImpl implements ICourseHistoryService {

    private final CourseHistoryRepository courseHistoryRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CourseHistoryServiceImpl(CourseHistoryRepository courseHistoryRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.courseHistoryRepository = courseHistoryRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseHistory getCourseHistoryByUserId(Long userId) {
        return courseHistoryRepository.findByUserCreator(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user id")));
    }

    @Override
    public List<CourseHistory> getCourseHistoryByEmail(String email) {
        return courseHistoryRepository.findAllByUserCreator(userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("Invalid user id")));
    }


    @Override
    public CourseHistory saveCourseInCourseHistory(Long userId, Long courseId, String courseData) {

        User foundUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user id"));

        CourseHistory foundCourseHistoryForUser = courseHistoryRepository.findByUserCreator(foundUser);
        if(foundCourseHistoryForUser != null) {
            foundCourseHistoryForUser.getCourses().add(courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Invalid course id")));
            foundCourseHistoryForUser.setCourseData(courseData);
        }else{
            foundCourseHistoryForUser = new CourseHistory(
                    userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Invalid user id")),
                    new ArrayList<>(),
                    "",
                    LocalDateTime.now()
            );
        }
        return courseHistoryRepository.save(foundCourseHistoryForUser);
    }
}

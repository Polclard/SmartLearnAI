package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.Course;
import org.smartlearnai.model.User;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.FavoriteCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteCourseServiceImpl implements FavoriteCourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public void addFavoriteCourse(String username, Long courseId) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        user.getFavoriteCourses().add(course);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeFavoriteCourse(String username, Long courseId) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        user.getFavoriteCourses().remove(course);
        userRepository.save(user);
    }

    @Override
    public Set<Course> getUserFavoriteCourses(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getFavoriteCourses();
    }
}


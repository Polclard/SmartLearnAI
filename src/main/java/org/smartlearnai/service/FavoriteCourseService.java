package org.smartlearnai.service;

import org.smartlearnai.model.Course;
import java.util.Set;

public interface FavoriteCourseService {
    void addFavoriteCourse(String username, Long courseId);
    void removeFavoriteCourse(String username, Long courseId);
    Set<Course> getUserFavoriteCourses(String username);
}

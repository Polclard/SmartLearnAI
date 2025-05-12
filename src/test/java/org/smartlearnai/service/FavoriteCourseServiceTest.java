package org.smartlearnai.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.smartlearnai.model.Course;
import org.smartlearnai.model.User;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.impl.FavoriteCourseServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FavoriteCourseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private FavoriteCourseServiceImpl favoriteCourseService;

    private User testUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");
        testUser.setFavoriteCourses(new HashSet<>());

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
    }

    @Test
    void testAddFavoriteCourse() {
        when(userRepository.findUserByEmail("testuser@example.com")).thenReturn(Optional.of(testUser));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        favoriteCourseService.addFavoriteCourse("testuser@example.com", 1L);

        assertTrue(testUser.getFavoriteCourses().contains(testCourse));
        verify(userRepository).save(testUser);
    }

    @Test
    void testRemoveFavoriteCourse() {
        testUser.getFavoriteCourses().add(testCourse);

        when(userRepository.findUserByEmail("testuser@example.com")).thenReturn(Optional.of(testUser));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        favoriteCourseService.removeFavoriteCourse("testuser@example.com", 1L);

        assertFalse(testUser.getFavoriteCourses().contains(testCourse));
        verify(userRepository).save(testUser);
    }

    @Test
    void testGetUserFavoriteCourses() {
        testUser.getFavoriteCourses().add(testCourse);

        when(userRepository.findUserByEmail("testuser@example.com")).thenReturn(Optional.of(testUser));

        Set<Course> favorites = favoriteCourseService.getUserFavoriteCourses("testuser@example.com");

        assertEquals(1, favorites.size());
        assertTrue(favorites.contains(testCourse));
    }
}


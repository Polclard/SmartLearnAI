package org.smartlearnai.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.smartlearnai.model.Course;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.exeptions.ResourceNotFoundException;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.service.impl.CourseServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void toggleFavoriteSuccessTest() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setFavourite(false);

        Course savedCourse = new Course();
        savedCourse.setId(courseId);
        savedCourse.setFavourite(true);

        CourseDto expectedDto = new CourseDto();
        expectedDto.setId(courseId);
        expectedDto.setFavorite(true);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        when(modelMapper.map(savedCourse, CourseDto.class)).thenReturn(expectedDto);

        CourseDto result = courseService.toggleFavorite(courseId);

        assertTrue(result.isFavorite());
        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(course);
        verify(modelMapper).map(savedCourse, CourseDto.class);
    }

    @Test
    void toggleFavoriteExceptionTest() {
        Long nonExistentId = 99L;
        when(courseRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> courseService.toggleFavorite(nonExistentId),
                "Expected toggleFavorite() to throw, but it didn't");

        verify(courseRepository, never()).save(any());
    }
}

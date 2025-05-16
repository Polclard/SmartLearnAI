package org.smartlearnai.service.impl;

import org.smartlearnai.model.Course;
import org.smartlearnai.model.Lesson;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.LessonDto;
import org.smartlearnai.model.exeptions.ResourceNotFoundException;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Override
    public CourseDto generateCourse(String title, String description, String promptUsed, List<LessonDto> lessonDTOs) {
        Course course = Course.builder()
                .title(title)
                .description(description)
                .promptUsed(promptUsed)
                .hasQuiz(true)
                .isFavourite(false)
                .build();

        List<Lesson> lessons = lessonDTOs.stream().map(lessonDto -> {
            Lesson lesson = new Lesson();
            lesson.setTitle(lessonDto.getTitle());
            lesson.setContent(lessonDto.getContent());
            lesson.setCourse(course);
            return lesson;
        }).collect(Collectors.toList());

        course.setLessons(lessons);

        Course savedCourse = courseRepository.save(course);

        return convertToDto(savedCourse, lessonDTOs);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return convertToDto(course);
    }

    @Override
    public CourseDto toggleFavorite(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        course.setFavourite(!course.isFavourite());
        Course updatedCourse = courseRepository.save(course);

        return convertToDto(updatedCourse);
    }

    @Override
    public List<CourseDto> filterCourse(String query) {
        List<Course> foundCourses;
        if (query == null || query.isBlank()) {
            foundCourses = courseRepository.findAll();
        } else {
            foundCourses = courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        }
        return foundCourses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Course entity to CourseDto with existing LessonDtos
    private CourseDto convertToDto(Course course, List<LessonDto> lessonDTOs) {
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .promptUsed(course.getPromptUsed())
                .hasQuiz(course.isHasQuiz())
                .isFavorite(course.isFavourite())
                .lessons(lessonDTOs)
                .build();
    }

    // Helper method to convert Course entity to CourseDto
    private CourseDto convertToDto(Course course) {
        List<LessonDto> lessonDtos = course.getLessons().stream()
                .map(lesson -> modelMapper.map(lesson, LessonDto.class))
                .collect(Collectors.toList());

        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .promptUsed(course.getPromptUsed())
                .hasQuiz(course.isHasQuiz())
                .isFavorite(course.isFavourite())
                .lessons(lessonDtos)
                .build();
    }
}
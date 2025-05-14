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
                .build();

        List<Lesson> lessons = lessonDTOs.stream().map(lessonDto -> {
            Lesson lesson = new Lesson();
            lesson.setTitle(lessonDto.getTitle());
            lesson.setContent(lessonDto.getContent());
            lesson.setCourse(course);
            return lesson;
        }).collect(Collectors.toList());

        course.setLessons(lessons);

        courseRepository.save(course);

        return CourseDto.builder()
                .id(courseRepository.findById(course.getId()).get().getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .promptUsed(course.getPromptUsed())
                .hasQuiz(true)
                .lessons(lessonDTOs)
                .build();
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDto.class))
                .toList();
    }

    @Override
    public CourseDto getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(course -> modelMapper.map(course, CourseDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
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
                .map(course -> modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }
}

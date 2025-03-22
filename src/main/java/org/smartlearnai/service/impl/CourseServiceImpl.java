package org.smartlearnai.service.impl;


import org.smartlearnai.model.Course;
import org.smartlearnai.model.Lesson;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.LessonDto;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Override
    public CourseDto generateCourse(String title, String description, String promptUsed, List<LessonDto> lessonDTOs) {
        LessonDto lesson1 = LessonDto.builder()
                .id(1L)
                .title("Lesson 1: Intro to AI")
                .content("Artificial Intelligence is a branch of computer science...")
                .orderInCourse(1)
                .build();

        LessonDto lesson2 = LessonDto.builder()
                .id(2L)
                .title("Lesson 2: History of AI")
                .content("AI dates back to the 1950s with pioneers like Turing...")
                .orderInCourse(2)
                .build();

        return CourseDto.builder()
                .id(999L)
                .title(title)
                .description(description)
                .promptUsed(promptUsed)
                .hasQuiz(true)
                .lessons(List.of(lesson1, lesson2))
                .build();
       /* Course course = Course.builder()
                .title(title)
                .description(description)
                .promptUsed(promptUsed)
                .hasQuiz(true)
                .build();

        List<Lesson> lessons = lessonDTOs.stream()
                .map(dto -> Lesson.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .orderInCourse(dto.getOrderInCourse())
                        .course(course)
                        .build())
                .toList();

        course.setLessons(lessons);
        Course saved = courseRepository.save(course);
        return modelMapper.map(saved, CourseDto.class);*/
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
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
}

package org.smartlearnai.controller;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.CourseRequest;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.CourseService;
import org.smartlearnai.service.impl.CourseHistoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseHistoryServiceImpl courseHistoryService;
    private final UserRepository userRepository;

    /*
        @PostMapping
        public ResponseEntity<CourseDto> generateCourse(
                @RequestParam String title,
                @RequestParam String description,
                @RequestParam String promptUsed,
                @RequestBody List<LessonDto> lessons
        ) {
            CourseDto courseDTO = courseService.generateCourse(title, description, promptUsed, lessons);
            return ResponseEntity.ok(courseDTO);
        }
    */
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDto> createCourse(
            @Valid @RequestBody CourseRequest request
    ) {
        CourseDto courseDTO = courseService.generateCourse(
                request.getTitle(),
                request.getDescription(),
                request.getPromptUsed(),
                request.getLessons()
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        courseHistoryService.saveCourseInCourseHistory(user.getId(), courseDTO.getId(), courseDTO.getPromptUsed());
        return ResponseEntity.ok(courseDTO);
    }

    @GetMapping("/courses/search")
    public ResponseEntity<?> searchCourses(
            @RequestParam(required = false) String query) {

        // Validate Input: Query is null or empty
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Query cannot be empty.");
        }

        // Validate Input: No Special Characters
        if (!query.matches("^[a-zA-Z0-9\\s]+$")) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid query - No special characters allowed");
        }

        // Search Courses
        List<CourseDto> courses = courseService.filterCourse(query);

        // Handle Empty Results (404 Not Found)
        if (courses.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("No courses found for the given query.");
        }

        return ResponseEntity.ok(courses);
    }


}
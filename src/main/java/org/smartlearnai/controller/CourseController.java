package org.smartlearnai.controller;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.CourseRequest;
import org.smartlearnai.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseDto courseDTO = courseService.generateCourse(
                request.getTitle(),
                request.getDescription(),
                request.getPromptUsed(),
                request.getLessons()
        );

        return ResponseEntity.ok(courseDTO);
    }

    @GetMapping("/toggle/{id}")
    public ResponseEntity<CourseDto> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.toggleFavorite(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDto>> searchCourses(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(courseService.filterCourse(query));
    }
}
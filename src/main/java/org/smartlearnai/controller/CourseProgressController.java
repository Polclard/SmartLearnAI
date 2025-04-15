package org.smartlearnai.controller;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.CourseProgress;
import org.smartlearnai.model.dto.CourseProgressDto;
import org.smartlearnai.service.ICourseProgress;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class CourseProgressController {

    private final ICourseProgress courseProgressService;

    @PostMapping("/save")
    public ResponseEntity<CourseProgress> saveCourseProgress(@RequestBody CourseProgressDto courseProgressDto) {
        CourseProgress savedProgress = courseProgressService.saveCourseProgress(courseProgressDto);
        return ResponseEntity.ok(savedProgress);
    }
}

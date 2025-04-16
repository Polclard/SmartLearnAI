package org.smartlearnai.controller;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.service.impl.CourseHistoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses_history")
@RequiredArgsConstructor
public class CourseHistoryController {

    private final CourseHistoryServiceImpl courseHistoryService;

    @GetMapping()
    public ResponseEntity<CourseHistory> getCourseHistory(@RequestBody Long userId) {
        CourseHistory historyList = courseHistoryService.getCourseHistoryByUserId(userId);
        return ResponseEntity.ok(historyList);
    }
}

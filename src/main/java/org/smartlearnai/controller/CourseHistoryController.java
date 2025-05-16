package org.smartlearnai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.User;
import org.smartlearnai.model.dto.CourseHistoryDto;
import org.smartlearnai.model.dto.CourseHistoryRequest;
import org.smartlearnai.model.dto.CourseHistoryUpdateRequest;
import org.smartlearnai.model.exeptions.ResourceNotFoundException;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.CourseHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-history")
@RequiredArgsConstructor
public class CourseHistoryController {

    private final CourseHistoryService courseHistoryService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<CourseHistoryDto> addCourseHistory(
            @Valid @RequestBody CourseHistoryRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CourseHistoryDto historyDto = courseHistoryService.saveCourseHistory(user.getId(), request);
        return new ResponseEntity<>(historyDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseHistoryDto> updateCourseHistoryPrompts(
            @PathVariable Long id,
            @Valid @RequestBody CourseHistoryUpdateRequest request) {

        // Verify the history entry belongs to the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the history belongs to the authenticated user
        CourseHistoryDto existingHistory = courseHistoryService.getCourseHistoryById(id);
        if (!existingHistory.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this history entry");
        }

        CourseHistoryDto updatedHistory = courseHistoryService.updateCourseHistoryPrompts(id, request.getPrompts());
        return ResponseEntity.ok(updatedHistory);
    }

    @GetMapping
    public ResponseEntity<List<CourseHistoryDto>> getCurrentUserHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<CourseHistoryDto> history = courseHistoryService.getCourseHistoryByUserId(user.getId());
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseHistoryDto> getCourseHistoryById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CourseHistoryDto historyDto = courseHistoryService.getCourseHistoryById(id);

        // Ensure the user can only access their own history
        if (!historyDto.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to access this history entry");
        }

        return ResponseEntity.ok(historyDto);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseHistoryDto>> getHistoryByCourse(@PathVariable Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<CourseHistoryDto> history = courseHistoryService.getCourseHistoryByUserIdAndCourseId(user.getId(), courseId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseHistoryDto>> searchHistoryByTitle(@RequestParam String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // First filter by title
        List<CourseHistoryDto> history = courseHistoryService.searchCourseHistoryByTitle(title);

        // Then filter by current user
        history = history.stream()
                .filter(h -> h.getUserId().equals(user.getId()))
                .toList();

        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseHistory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CourseHistoryDto historyDto;
        try {
            historyDto = courseHistoryService.getCourseHistoryById(id);
        } catch (ResourceNotFoundException e) {
            // If not found, just return no content
            return ResponseEntity.noContent().build();
        }

        // Ensure the user can only delete their own history
        if (!historyDto.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this history entry");
        }

        courseHistoryService.deleteCourseHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Admin endpoints - restricted by Spring Security config
    @GetMapping("/admin/all")
    public ResponseEntity<List<CourseHistoryDto>> getAllCourseHistory() {
        return ResponseEntity.ok(courseHistoryService.getAllCourseHistory());
    }

    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<List<CourseHistoryDto>> getUserCourseHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(courseHistoryService.getCourseHistoryByUserId(userId));
    }
}
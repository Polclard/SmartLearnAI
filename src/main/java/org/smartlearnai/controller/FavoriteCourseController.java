package org.smartlearnai.controller;

import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.Course;
import org.smartlearnai.service.FavoriteCourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteCourseController {

    private final FavoriteCourseService favoriteCourseService;

    //  Add Course to User's Favorites
    @PostMapping("/{courseId}")
    public ResponseEntity<String> addFavoriteCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        favoriteCourseService.addFavoriteCourse(userDetails.getUsername(), courseId);
        return ResponseEntity.ok("Course added to favorites");
    }

    // Remove Course from User's Favorites
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> removeFavoriteCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        favoriteCourseService.removeFavoriteCourse(userDetails.getUsername(), courseId);
        return ResponseEntity.ok("Course removed from favorites");
    }

    //  Get All User's Favorite Courses
    @GetMapping
    public ResponseEntity<Set<Course>> getUserFavoriteCourses(
            @AuthenticationPrincipal UserDetails userDetails) {
        Set<Course> favoriteCourses = favoriteCourseService.getUserFavoriteCourses(userDetails.getUsername());
        return ResponseEntity.ok(favoriteCourses);
    }
}


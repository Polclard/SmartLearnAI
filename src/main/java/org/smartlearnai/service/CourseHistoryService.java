package org.smartlearnai.service;

import org.smartlearnai.model.Message;
import org.smartlearnai.model.dto.CourseHistoryDto;
import org.smartlearnai.model.dto.CourseHistoryRequest;

import java.util.List;

public interface CourseHistoryService {

    // Save course interaction to history
    CourseHistoryDto saveCourseInCourseHistory(Long userId, Long courseId, List<Message> prompts);

    // Save with request object
    CourseHistoryDto saveCourseHistory(Long userId, CourseHistoryRequest request);

    // Update prompts for an existing history entry
    CourseHistoryDto updateCourseHistoryPrompts(Long id, List<Message> prompts);

    // Get all history entries
    List<CourseHistoryDto> getAllCourseHistory();

    // Get history by ID
    CourseHistoryDto getCourseHistoryById(Long id);

    // Get history for a user
    List<CourseHistoryDto> getCourseHistoryByUserId(Long userId);

    // Get history for a course
    List<CourseHistoryDto> getCourseHistoryByCourseId(Long courseId);

    // Get history for a user and course
    List<CourseHistoryDto> getCourseHistoryByUserIdAndCourseId(Long userId, Long courseId);

    // Search course history by title
    List<CourseHistoryDto> searchCourseHistoryByTitle(String title);

    // Delete history entry
    void deleteCourseHistory(Long id);
}
package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.model.Message;
import org.smartlearnai.model.dto.CourseHistoryDto;
import org.smartlearnai.model.dto.CourseHistoryRequest;
import org.smartlearnai.model.exeptions.ResourceNotFoundException;
import org.smartlearnai.repository.CourseHistoryRepository;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.service.CourseHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseHistoryServiceImpl implements CourseHistoryService {

    private final CourseHistoryRepository courseHistoryRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Override
    public CourseHistoryDto saveCourseInCourseHistory(Long userId, Long courseId, List<Message> prompts) {
        String courseTitle = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId))
                .getTitle();

        CourseHistory courseHistory = CourseHistory.builder()
                .userId(userId)
                .courseId(courseId)
                .courseTitle(courseTitle)
                .prompts(prompts)
                .build();

        CourseHistory savedHistory = courseHistoryRepository.save(courseHistory);
        return convertToDto(savedHistory);
    }

    @Override
    public CourseHistoryDto saveCourseHistory(Long userId, CourseHistoryRequest request) {
        CourseHistory courseHistory = CourseHistory.builder()
                .userId(userId)
                .courseId(request.getCourseId())
                .courseTitle(request.getCourseTitle())
                .prompts(request.getPrompts())
                .build();

        CourseHistory savedHistory = courseHistoryRepository.save(courseHistory);
        return convertToDto(savedHistory);
    }

    @Override
    public CourseHistoryDto updateCourseHistoryPrompts(Long id, List<Message> prompts) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));

        courseHistory.setPrompts(prompts);
        CourseHistory updatedHistory = courseHistoryRepository.save(courseHistory);

        return convertToDto(updatedHistory);
    }

    @Override
    public List<CourseHistoryDto> getAllCourseHistory() {
        return courseHistoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseHistoryDto getCourseHistoryById(Long id) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));
        return convertToDto(courseHistory);
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByUserId(Long userId) {
        return courseHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByCourseId(Long courseId) {
        return courseHistoryRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByUserIdAndCourseId(Long userId, Long courseId) {
        return courseHistoryRepository.findByUserIdAndCourseId(userId, courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> searchCourseHistoryByTitle(String title) {
        return courseHistoryRepository.findByCourseTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourseHistory(Long id) {
        if (!courseHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course history not found with id: " + id);
        }
        courseHistoryRepository.deleteById(id);
    }

    private CourseHistoryDto convertToDto(CourseHistory courseHistory) {
        return CourseHistoryDto.builder()
                .id(courseHistory.getId())
                .courseId(courseHistory.getCourseId())
                .userId(courseHistory.getUserId())
                .courseTitle(courseHistory.getCourseTitle())
                .prompts(courseHistory.getPrompts())
                .createdAt(courseHistory.getCreatedAt())
                .build();
    }
}
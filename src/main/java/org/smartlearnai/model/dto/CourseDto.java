package org.smartlearnai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String promptUsed; // Changed from List<Message> to String
    private List<LessonDto> lessons;
    private boolean hasQuiz;
    private boolean isFavorite;
}
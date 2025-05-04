package org.smartlearnai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String promptUsed;
    private boolean hasQuiz;
    private List<LessonDto> lessons;
    private boolean isFavorite;


}
package org.smartlearnai.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @NotBlank(message = "Course title is required")
    private String title;

    @NotBlank(message = "Course description is required")
    private String description;

    @NotBlank(message = "Prompt is required")
    private String promptUsed;

    @NotEmpty(message = "Lessons list cannot be empty")
    private List<LessonDto> lessons;
}

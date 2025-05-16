package org.smartlearnai.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
public class CourseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Prompt is required")
    @Size(max = 1000, message = "Prompt cannot exceed 1000 characters")
    private String promptUsed; // Changed from List<Message> to String

    private List<LessonDto> lessons;
}
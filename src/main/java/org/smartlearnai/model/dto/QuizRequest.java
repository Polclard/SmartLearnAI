package org.smartlearnai.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRequest {

    @NotBlank(message = "Quiz title is required")
    private String title;

    @NotBlank(message = "Quiz description is required")
    private String description;

    private boolean isPublished;

    @NotEmpty(message = "Questions list cannot be empty")
    private List<QuestionDto> questions;
}

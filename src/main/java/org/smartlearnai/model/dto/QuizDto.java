package org.smartlearnai.model.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private boolean isPublished;
    private List<QuestionDto> questions;
}

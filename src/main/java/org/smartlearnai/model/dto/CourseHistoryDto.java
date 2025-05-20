package org.smartlearnai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartlearnai.model.Message;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseHistoryDto {
    private Long id;
    private String courseTitle;
    private Long courseId;
    private Long userId;
    private List<Message> prompts;
    private LocalDateTime createdAt;
    private boolean isFavorite;
}
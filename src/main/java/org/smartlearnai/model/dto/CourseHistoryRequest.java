package org.smartlearnai.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartlearnai.model.Message;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseHistoryRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "Course title is required")
    private String courseTitle;

    @NotEmpty(message = "At least one message in the prompts is required")
    private List<Message> prompts;
}
package org.smartlearnai.model.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgressDto {

    private Long id;
    private Long userId;
    private Long courseId;
    private int completedLessons;
    private boolean quizPassed;
}

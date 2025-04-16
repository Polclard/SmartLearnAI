package org.smartlearnai.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartlearnai.model.User;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseHistoryDto {
    private Long id;
    private User userCreator;
    private LocalDateTime creationDate;
    private String courseData;
}

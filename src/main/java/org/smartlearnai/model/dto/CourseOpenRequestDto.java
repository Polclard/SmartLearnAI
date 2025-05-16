
package org.smartlearnai.model.dto;

import lombok.Data;
import org.smartlearnai.model.Message;

import java.util.List;

@Data
public class CourseOpenRequestDto {
    private Long userId;
    private Long courseId;
    private List<Message> courseData;
}

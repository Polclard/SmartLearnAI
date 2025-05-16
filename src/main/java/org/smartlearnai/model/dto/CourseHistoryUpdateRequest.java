
package org.smartlearnai.model.dto;

import jakarta.validation.constraints.NotEmpty;
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
public class CourseHistoryUpdateRequest {

    @NotEmpty(message = "Prompts cannot be empty")
    private List<Message> prompts;
}
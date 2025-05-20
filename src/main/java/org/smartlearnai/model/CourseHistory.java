package org.smartlearnai.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "course_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseTitle;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private Long userId;

    @Lob
    @Column(length = 10000)
    @Convert(converter = MessageListConverter.class)
    private List<Message> prompts;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean isFavorite = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
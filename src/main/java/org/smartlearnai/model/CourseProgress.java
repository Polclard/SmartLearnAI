package org.smartlearnai.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public CourseProgress(User user, Course course, int completedLessons, boolean quizPassed) {
        this.user = user;
        this.course = course;
        this.completedLessons = completedLessons;
        this.quizPassed = quizPassed;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private int completedLessons;

    private boolean quizPassed;
}

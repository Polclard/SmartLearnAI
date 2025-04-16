package org.smartlearnai.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_creator_id")
    private User userCreator;

    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @Lob
    @Column()
    private String courseData;

    public CourseHistory(User userCreator, List<Course> courses, String courseData, LocalDateTime creationDate) {
        this.courses = courses;
        this.courseData = courseData;
        this.creationDate = creationDate;
        this.userCreator = userCreator;
    }
}

//package org.smartlearnai.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class CourseHistory {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_creator_id")
//    private User userCreator;
//
//    private LocalDateTime creationDate;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Course> courses = new ArrayList<>();
//
//    @Lob
//    @Column()
//    @ElementCollection
//    private List<Message> courseData;
//
//    public CourseHistory(User userCreator, List<Course> courses, List<Message> courseData, LocalDateTime creationDate) {
//        this.courses = courses;
//        this.courseData = courseData;
//        this.creationDate = creationDate;
//        this.userCreator = userCreator;
//    }
//}
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
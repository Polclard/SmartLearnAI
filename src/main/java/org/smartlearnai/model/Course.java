package org.smartlearnai.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String description;

    @Column(length = 1000)
    private String promptUsed; // Changed from List<Message> to String

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    private boolean hasQuiz;

    private boolean isFavourite;

    // Constructors
    public Course() {}

    public Course(String title, String description, String promptUsed, boolean hasQuiz) {
        this.title = title;
        this.description = description;
        this.promptUsed = promptUsed;
        this.hasQuiz = hasQuiz;
        this.isFavourite = false;
    }
}
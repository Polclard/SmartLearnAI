package org.smartlearnai.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String promptUsed;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    private boolean hasQuiz;

    private boolean isFavourite;

    @JsonIgnore
    @ManyToMany(mappedBy = "favoriteCourses", fetch = FetchType.LAZY)
    private Set<User> favoritedByUsers = new HashSet<>();  // Users who favorited this course


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

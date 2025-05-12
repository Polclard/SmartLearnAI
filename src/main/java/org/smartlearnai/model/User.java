package org.smartlearnai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String role;
    private int age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorites", // Join table name
            joinColumns = @JoinColumn(name = "user_id"),  // Foreign key column in 'favorites' table pointing to 'User'
            inverseJoinColumns = @JoinColumn(name = "course_id") // Foreign key column pointing to 'Course'
    )
    private Set<Course> favoriteCourses = new HashSet<>();  // Favorite courses for this user

}

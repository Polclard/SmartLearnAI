package org.smartlearnai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartlearnai.model.Course;
import org.smartlearnai.model.User;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.FavoriteCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FavoriteCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoriteCourseService favoriteCourseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private User testUser;
    private Course testCourse;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        courseRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);

        testCourse = new Course();
        testCourse.setTitle("Test Course");
        testCourse.setDescription("Test Description");
        testCourse.setPromptUsed("AI Prompt");
        courseRepository.save(testCourse);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testAddFavoriteCourse() throws Exception {
        mockMvc.perform(post("/api/favorites/" + testCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Course added to favorites"));

        // Test Get after Add
        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Course"));

        // Test Remove
        mockMvc.perform(delete("/api/favorites/" + testCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Course removed from favorites"));

        // Verify course is removed
        mockMvc.perform(get("/api/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}

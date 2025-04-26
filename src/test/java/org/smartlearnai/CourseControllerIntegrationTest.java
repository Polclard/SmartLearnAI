package org.smartlearnai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.smartlearnai.model.User;
import org.smartlearnai.model.dto.CourseDto;
import org.smartlearnai.model.dto.CourseRequest;
import org.smartlearnai.model.dto.LessonDto;
import org.smartlearnai.repository.CourseHistoryRepository;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.repository.UserRepository;
import org.smartlearnai.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    private User testUser;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseHistoryRepository courseHistoryRepository;

    @BeforeEach
    public void setup() {
        // Setup a user for authentication testing
        testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        userRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        courseHistoryRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testCreateCourse() throws Exception {
        // Prepare the CourseRequest DTO to send in the body
        CourseRequest request = new CourseRequest();
        request.setTitle("AI Course");
        request.setDescription("An introductory course on AI.");
        request.setPromptUsed("AI generation prompt");
        request.setLessons(List.of(
                LessonDto.builder().title("Lesson 1: Introduction").content("Content of lesson 1").orderInCourse(1).build(),
                LessonDto.builder().title("Lesson 2: Advanced AI").content("Content of lesson 2").orderInCourse(1).build()
        ));

        String jsonRequest = objectMapper.writeValueAsString(request);

        // Perform POST request
        MvcResult result = mockMvc.perform(post("/api/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("AI Course"))
                .andExpect(jsonPath("$.description").value("An introductory course on AI."))
                .andExpect(jsonPath("$.lessons", hasSize(2)))
                .andReturn();

        // Optionally, you can verify that the course was saved in the database by querying it
        String response = result.getResponse().getContentAsString();
        CourseDto courseDto = objectMapper.readValue(response, CourseDto.class);
        // Validate if the course was correctly created and saved (e.g., by checking the ID)
        assert courseDto.getId() != null;
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testGetAllCourses() throws Exception {
        // Save a course to test the retrieval functionality
        CourseRequest request = new CourseRequest();
        request.setTitle("AI Course");
        request.setDescription("An introductory course on AI.");
        request.setPromptUsed("AI generation prompt");
        request.setLessons(List.of(
                LessonDto.builder().title("Lesson 1: Introduction").content("Content of lesson 1").orderInCourse(1).build(),
                LessonDto.builder().title("Lesson 2: Advanced AI").content("Content of lesson 2").orderInCourse(1).build()
        ));

        // Use the service directly or create the course manually here
        courseService.generateCourse(request.getTitle(), request.getDescription(), request.getPromptUsed(), request.getLessons());

        // Perform GET request to fetch all courses
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))  // Assuming there is one course in DB
                .andExpect(jsonPath("$[0].title").value("AI Course"));
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testGetCourseById() throws Exception {
        // Save a course to test the retrieval by ID
        CourseRequest request = new CourseRequest();
        request.setTitle("AI Course");
        request.setDescription("An introductory course on AI.");
        request.setPromptUsed("AI generation prompt");
        request.setLessons(List.of(
                LessonDto.builder().title("Lesson 1: Introduction").content("Content of lesson 1").orderInCourse(1).build(),
                LessonDto.builder().title("Lesson 2: Advanced AI").content("Content of lesson 2").orderInCourse(1).build()
        ));

        CourseDto createdCourse = courseService.generateCourse(request.getTitle(), request.getDescription(), request.getPromptUsed(), request.getLessons());

        // Perform GET request to fetch the course by its ID
        mockMvc.perform(get("/api/courses/{id}", createdCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("AI Course"))
                .andExpect(jsonPath("$.description").value("An introductory course on AI."));
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    public void testGetCourseByIdNotFound() throws Exception {
        // Try fetching a non-existing course by ID
        mockMvc.perform(get("/api/courses/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}


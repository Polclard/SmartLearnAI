package org.smartlearnai.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartlearnai.model.dto.QuizDto;
import org.smartlearnai.model.dto.QuizRequest;
import org.smartlearnai.service.QuizService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/test")
    public ResponseEntity<QuizDto> generateTestQuiz() {
        return ResponseEntity.ok(quizService.generateTestQuiz());
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizById(id));
    }

    @PostMapping("/{id}/score")
    public ResponseEntity<Integer> scoreQuiz(@PathVariable Long id, @RequestBody Map<Long, String> answers) {
        int score = quizService.scoreQuiz(id, answers);
        return ResponseEntity.ok(score);
    }

    @PostMapping
    public ResponseEntity<QuizDto> createQuiz(@Valid @RequestBody QuizRequest request) {
        QuizDto quizDto = quizService.createQuiz(request);
        return ResponseEntity.ok(quizDto);
    }
}

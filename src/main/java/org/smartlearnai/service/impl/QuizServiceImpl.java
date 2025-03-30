package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.smartlearnai.model.Question;
import org.smartlearnai.model.Quiz;
import org.smartlearnai.model.dto.QuestionDto;
import org.smartlearnai.model.dto.QuizDto;
import org.smartlearnai.model.dto.QuizRequest;
import org.smartlearnai.repository.QuizRepository;
import org.smartlearnai.service.QuizService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;

    @Override
    public QuizDto generateTestQuiz() {
        Quiz quiz = Quiz.builder()
                .title("Spring Boot Basics")
                .description("Test your knowledge of Spring Boot fundamentals.")
                .isPublished(true)
                .build();

        List<Question> questions = List.of(
                Question.builder()
                        .content("What annotation is used to mark a Spring Boot application?")
                        .correctAnswer("@SpringBootApplication")
                        .quiz(quiz)
                        .build(),
                Question.builder()
                        .content("Which dependency is required for Spring Web?")
                        .correctAnswer("spring-boot-starter-web")
                        .quiz(quiz)
                        .build()
        );

        quiz.setQuestions(questions);
        Quiz saved = quizRepository.save(quiz);
        return modelMapper.map(saved, QuizDto.class);
    }

    @Override
    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(q -> modelMapper.map(q, QuizDto.class))
                .toList();
    }

    @Override
    public QuizDto getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
        return modelMapper.map(quiz, QuizDto.class);
    }

    @Override
    public int scoreQuiz(Long quizId, Map<Long, String> submittedAnswers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        int score = 0;
        for (Question q : quiz.getQuestions()) {
            String submitted = submittedAnswers.get(q.getId());
            if (q.getCorrectAnswer().equalsIgnoreCase(submitted)) {
                score++;
            }
        }
        return score;
    }

    @Override
    public QuizDto createQuiz(QuizRequest request) {
        Quiz quiz = Quiz.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isPublished(request.isPublished())
                .build();

        List<Question> questions = request.getQuestions().stream()
                .map(dto -> Question.builder()
                        .content(dto.getContent())
                        .correctAnswer(dto.getCorrectAnswer())
                        .quiz(quiz)
                        .build())
                .toList();

        quiz.setQuestions(questions);
        Quiz saved = quizRepository.save(quiz);
        return modelMapper.map(saved, QuizDto.class);
    }
}
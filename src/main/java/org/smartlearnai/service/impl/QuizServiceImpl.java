package org.smartlearnai.service.impl;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.smartlearnai.model.Question;
import org.smartlearnai.model.Quiz;
import org.smartlearnai.model.dto.QuizDto;
import org.smartlearnai.model.dto.QuizRequest;
import org.smartlearnai.repository.QuizRepository;
import org.smartlearnai.service.QuizService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


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

    @Override
    public byte[] exportQuizToPdf(Long quizId, Map<Long, String> userAnswers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font questionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font answerFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Quiz Title: " + quiz.getTitle(), titleFont));
            document.add(new Paragraph("Description: " + quiz.getDescription()));
            document.add(new Paragraph(" "));

            int i = 1;
            for (Question q : quiz.getQuestions()) {
                document.add(new Paragraph(i++ + ". " + q.getContent(), questionFont));
                String userAnswer = userAnswers.getOrDefault(q.getId(), "No answer selected");
                document.add(new Paragraph("Your Answer: " + userAnswer, answerFont));
                document.add(new Paragraph("Correct Answer: " + q.getCorrectAnswer(), answerFont));
                document.add(new Paragraph(" "));
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }



}
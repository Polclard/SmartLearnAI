package org.smartlearnai.service;

import org.smartlearnai.model.dto.QuizDto;
import org.smartlearnai.model.dto.QuizRequest;

import java.util.List;
import java.util.Map;

public interface QuizService {
    QuizDto generateTestQuiz();
    List<QuizDto> getAllQuizzes();
    QuizDto getQuizById(Long id);
    int scoreQuiz(Long quizId, Map<Long, String> submittedAnswers);
    QuizDto createQuiz(QuizRequest request);
}

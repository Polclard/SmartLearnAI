package org.smartlearnai.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.smartlearnai.model.CourseHistory;
import org.smartlearnai.model.Message;
import org.smartlearnai.model.dto.CourseHistoryDto;
import org.smartlearnai.model.dto.CourseHistoryRequest;
import org.smartlearnai.model.exeptions.ResourceNotFoundException;
import org.smartlearnai.repository.CourseHistoryRepository;
import org.smartlearnai.repository.CourseRepository;
import org.smartlearnai.service.CourseHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CourseHistoryServiceImpl implements CourseHistoryService {

    private final CourseHistoryRepository courseHistoryRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    @Override
    public CourseHistoryDto saveCourseInCourseHistory(Long userId, Long courseId, List<Message> prompts) {
        String courseTitle = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId))
                .getTitle();

        CourseHistory courseHistory = CourseHistory.builder()
                .userId(userId)
                .courseId(courseId)
                .courseTitle(courseTitle)
                .prompts(prompts)
                .isFavorite(false) // Default to false
                .build();

        CourseHistory savedHistory = courseHistoryRepository.save(courseHistory);
        return convertToDto(savedHistory);
    }

    @Override
    public CourseHistoryDto saveCourseHistory(Long userId, CourseHistoryRequest request) {
        CourseHistory courseHistory = CourseHistory.builder()
                .userId(userId)
                .courseId(request.getCourseId())
                .courseTitle(request.getCourseTitle())
                .prompts(request.getPrompts())
                .isFavorite(false) // Default to false
                .build();

        CourseHistory savedHistory = courseHistoryRepository.save(courseHistory);
        return convertToDto(savedHistory);
    }

    @Override
    public CourseHistoryDto updateCourseHistoryPrompts(Long id, List<Message> prompts) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));

        courseHistory.setPrompts(prompts);
        CourseHistory updatedHistory = courseHistoryRepository.save(courseHistory);

        return convertToDto(updatedHistory);
    }

    @Override
    public List<CourseHistoryDto> getAllCourseHistory() {
        return courseHistoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseHistoryDto getCourseHistoryById(Long id) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));
        return convertToDto(courseHistory);
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByUserId(Long userId) {
        return courseHistoryRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByCourseId(Long courseId) {
        return courseHistoryRepository.findByCourseId(courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> getCourseHistoryByUserIdAndCourseId(Long userId, Long courseId) {
        return courseHistoryRepository.findByUserIdAndCourseId(userId, courseId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseHistoryDto> searchCourseHistoryByTitle(String title) {
        return courseHistoryRepository.findByCourseTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourseHistory(Long id) {
        if (!courseHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course history not found with id: " + id);
        }
        courseHistoryRepository.deleteById(id);
    }

    @Override
    public CourseHistoryDto toggleFavoriteStatus(Long id, boolean isFavorite) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));

        courseHistory.setFavorite(isFavorite);
        CourseHistory updatedHistory = courseHistoryRepository.save(courseHistory);

        return convertToDto(updatedHistory);
    }

    @Override
    public List<CourseHistoryDto> getFavoriteCourseHistoryByUserId(Long userId) {
        return courseHistoryRepository.findByUserIdAndIsFavoriteTrue(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportHistoryToPdf(Long id) {
        CourseHistory courseHistory = courseHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course history not found with id: " + id));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Using direct Font constructor instead of FontFactory
            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph(courseHistory.getCourseTitle(), titleFont));

            // Add date
            Font italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            document.add(new Paragraph("Created: " + courseHistory.getCreatedAt().format(formatter), italicFont));

            document.add(new Paragraph("\n"));

            // Add conversation
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            for (Message message : courseHistory.getPrompts()) {
                // Add sender name in bold
                document.add(new Paragraph(message.getRole().toUpperCase() + ":", boldFont));

                // Add message content with proper handling for null content
                String content = message.getContent() != null ? message.getContent() : "";
                document.add(new Paragraph(content, normalFont));

                // Add some spacing
                document.add(new Paragraph("\n"));
            }

            document.close();
            return outputStream.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private CourseHistoryDto convertToDto(CourseHistory courseHistory) {
        return CourseHistoryDto.builder()
                .id(courseHistory.getId())
                .courseId(courseHistory.getCourseId())
                .userId(courseHistory.getUserId())
                .courseTitle(courseHistory.getCourseTitle())
                .prompts(courseHistory.getPrompts())
                .createdAt(courseHistory.getCreatedAt())
                .isFavorite(courseHistory.isFavorite())
                .build();
    }
}
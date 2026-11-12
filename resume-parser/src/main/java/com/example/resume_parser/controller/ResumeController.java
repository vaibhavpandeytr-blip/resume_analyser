package com.example.resume_parser.controller;
import com.example.resume_parser.model.Resume;
import com.example.resume_parser.repository.ResumeRepository;
import com.example.resume_parser.service.TextExtractionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final TextExtractionService extractionService;
    private final ResumeRepository resumeRepository;

    public ResumeController(TextExtractionService extractionService,
                            ResumeRepository resumeRepository) {
        this.extractionService = extractionService;
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/upload")
    public Resume upload(@RequestParam("file") MultipartFile file) throws Exception {
        String text = extractionService.extractText(file);

        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setRawText(text);

        return resumeRepository.save(resume);
    }
}


package com.example.resume_parser.service;

import com.example.resume_parser.dto.MatchResult;
import com.example.resume_parser.dto.SubmissionView;
import com.example.resume_parser.entity.Resume;
import com.example.resume_parser.entity.Submission;
import com.example.resume_parser.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SubmissionService {

    private final TextExtractionService extractionService;
    private final ResumeService resumeService;
    private final MatchingService matchingService;
    private final SubmissionRepository submissionRepository;

    public SubmissionService(TextExtractionService extractionService,
                             ResumeService resumeService,
                             MatchingService matchingService,
                             SubmissionRepository submissionRepository) {
        this.extractionService = extractionService;
        this.resumeService = resumeService;
        this.matchingService = matchingService;
        this.submissionRepository = submissionRepository;
    }

    public MatchResult submit(MultipartFile file, String email, String jobDescription) throws Exception {
        validate(file, email, jobDescription);
        String cleanEmail = email.trim().toLowerCase();

        // 1. Tika: file -> text
        String text = extractionService.extractText(file);
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Could not read any text from this file");
        }

        // 2. Ollama parse + save resume
        Resume resume = resumeService.analyseAndSave(file.getOriginalFilename(), text);

        // 3. Match against the job description
        MatchResult result = matchingService.match(resume.getId(), jobDescription);

        // 4. Record the submission
        Submission submission = new Submission();
        submission.setEmail(cleanEmail);
        submission.setResume(resume);
        submission.setMatchPercent(result.matchPercent());
        submissionRepository.save(submission);

        return result;
    }

    public List<SubmissionView> history(String email) {
        return submissionRepository
                .findByEmailOrderBySubmittedAtDesc(email.trim().toLowerCase())
                .stream()
                .map(s -> new SubmissionView(
                        s.getId(),
                        s.getResume().getFileName(),
                        s.getMatchPercent(),
                        s.getSubmittedAt()))
                .toList();
    }

    private void validate(MultipartFile file, String email, String jobDescription) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a resume file");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Please enter a valid email");
        }
        if (jobDescription == null || jobDescription.isBlank()) {
            throw new IllegalArgumentException("Please enter a job description");
        }
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        if (!(name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".doc") || name.endsWith(".txt"))) {
            throw new IllegalArgumentException("Only PDF, DOC, DOCX or TXT files are supported");
        }
    }
}
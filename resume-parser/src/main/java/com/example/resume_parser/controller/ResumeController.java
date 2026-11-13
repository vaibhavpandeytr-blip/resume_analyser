package com.example.resume_parser.controller;

import com.example.resume_parser.dto.MatchResult;
import com.example.resume_parser.service.SubmissionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final SubmissionService submissionService;

    public ResumeController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MatchResult upload(@RequestParam("file") MultipartFile file,
                              @RequestParam String email,
                              @RequestParam String jobDescription) throws Exception {
        return submissionService.submit(file, email, jobDescription);
    }
}
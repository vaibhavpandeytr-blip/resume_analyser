package com.example.resume_parser.controller;

import com.example.resume_parser.dto.SubmissionView;
import com.example.resume_parser.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public List<SubmissionView> history(@RequestParam String email) {
        return submissionService.history(email);
    }
}
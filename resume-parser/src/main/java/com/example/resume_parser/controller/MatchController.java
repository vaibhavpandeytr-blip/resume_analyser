package com.example.resume_parser.controller;

import com.example.resume_parser.dto.MatchResult;
import com.example.resume_parser.service.MatchingService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MatchController {

    private final MatchingService matchingService;

    public MatchController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/resumes/{id}/match")
    public MatchResult match(@PathVariable Long id, @RequestBody String jobDescription) {
        return matchingService.match(id, jobDescription);
    }
}
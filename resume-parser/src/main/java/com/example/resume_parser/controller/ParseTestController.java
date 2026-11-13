package com.example.resume_parser.controller;

import com.example.resume_parser.dto.ParsedResume;
import com.example.resume_parser.entity.Resume;
import com.example.resume_parser.service.ResumeParsingService;
import com.example.resume_parser.service.ResumeService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParseTestController {

    private final ResumeParsingService parsingService;
    private final ResumeService resumeService;

    public ParseTestController(ResumeParsingService parsingService, ResumeService resumeService) {
        this.parsingService = parsingService;
        this.resumeService = resumeService;
    }

    @PostMapping("/test/parse2")
    public ParsedResume test(@RequestBody String text) {
        return parsingService.parse(text);
    }

    @PostMapping("/test/analyse2")
    public Resume analyse(@RequestParam String fileName, @RequestBody String text) {
        return resumeService.analyseAndSave(fileName, text);
    }
}
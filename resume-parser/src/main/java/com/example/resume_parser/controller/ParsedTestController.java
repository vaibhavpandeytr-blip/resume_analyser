package com.example.resume_parser.controller;

import com.example.resume_parser.dto.ParsedResume;
import com.example.resume_parser.service.ResumeParsingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParsedTestController {
    private final ResumeParsingService service;

    public ParsedTestController(ResumeParsingService service) {
        this.service = service;
    }

    @PostMapping("/test/parse")
    public ParsedResume test(@RequestBody String text) {
        return service.parse(text);
    }
}

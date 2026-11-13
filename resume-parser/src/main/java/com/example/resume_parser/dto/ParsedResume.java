package com.example.resume_parser.dto;

import java.util.List;

public record ParsedResume(
        String name,
        String email,
        String phone,
        List<String> skills,
        List<String> education,
        List<String> experience
) {}
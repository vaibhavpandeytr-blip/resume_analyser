package com.example.resume_parser.dto;

import java.time.LocalDateTime;

public record SubmissionView(
        Long id,
        String fileName,
        int matchPercent,
        LocalDateTime submittedAt
) {}
package com.example.resume_parser.dto;
import java.util.List;
public record MatchResult (
    Long resumeId,
    String candidateName,
    int matchPercent,
    List <String> matchedSkills,
    List<String> missingSkills
){}

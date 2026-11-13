package com.example.resume_parser.service;

import com.example.resume_parser.dto.JobRequirement;
import com.example.resume_parser.dto.MatchResult;
import com.example.resume_parser.entity.Resume;
import com.example.resume_parser.repository.ResumeRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    private final ChatClient chatClient;
    private final ResumeRepository repository;

    public MatchingService(ChatClient.Builder builder, ResumeRepository repository) {
        this.chatClient = builder.build();
        this.repository = repository;
    }

    public MatchResult match(Long resumeId, String jobDescription) {
        Resume resume = repository.findById(resumeId)
                .orElseThrow(() -> new NoSuchElementException("Resume not found: " + resumeId));

        // 1. Ask the model for the skills the job requires
        JobRequirement requirements = chatClient.prompt()
                .system("""
                        You extract required skills from job descriptions.
                        Return ONLY valid JSON with one field:
                        requiredSkills (array of short skill names, like "Java" or "PostgreSQL").
                        Use only skills mentioned in the text. Do not add explanations.
                        """)
                .user("Job description:\n" + jobDescription)
                .call()
                .entity(JobRequirement.class);

        List<String> required = requirements.requiredSkills() == null
                ? List.of() : requirements.requiredSkills();

        // 2. Turn the saved comma-separated skills back into a set
        Set<String> candidateSkills = new HashSet<>();
        if (resume.getSkills() != null) {
            for (String s : resume.getSkills().split(",")) {
                candidateSkills.add(s.trim().toLowerCase());
            }
        }

        // 3. Compare in plain Java
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        for (String skill : required) {
            if (candidateSkills.contains(skill.trim().toLowerCase())) {
                matched.add(skill);
            } else {
                missing.add(skill);
            }
        }

        int percent = required.isEmpty() ? 0 : (matched.size() * 100) / required.size();

        return new MatchResult(resume.getId(), resume.getName(), percent, matched, missing);
    }
}
package com.example.resume_parser.service;

import com.example.resume_parser.dto.ParsedResume;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ResumeParsingService {

    private final ChatClient chatClient;

    public ResumeParsingService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ParsedResume parse(String resumeText) {
        return chatClient.prompt()
                .system("""
                        You extract structured data from resume text.
                        Return ONLY valid JSON with these fields:
                        name (string),
                        email (string),
                        phone (string),
                        skills (array of short skill names, like "Java" or "PostgreSQL"),
                        education (array of strings, one per degree or school),
                        experience (array of strings, one per job or internship).
                        Use only information present in the text.
                        If a field is missing, use null or an empty array.
                        Do not add explanations.
                        """)
                .user("Resume text:\n" + resumeText)
                .call()
                .entity(ParsedResume.class);
    }
}
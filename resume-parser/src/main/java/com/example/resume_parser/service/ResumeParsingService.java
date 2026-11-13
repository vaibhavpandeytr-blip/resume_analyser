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
        You extract structured data from resumes.
        Return ONLY valid JSON with exactly these fields:
        name (string), email (string), phone (string),
        skills (array of strings), education (array of strings),
        experience (array of strings).
        Use only information present in the text.
        If something is missing, use null for text fields or [] for lists.
        Do not add explanations.
        """)
                .user("Resume text:\n" + resumeText)
                .call()
                .entity(ParsedResume.class);
    }
}


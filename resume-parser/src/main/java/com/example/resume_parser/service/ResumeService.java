package com.example.resume_parser.service;

import com.example.resume_parser.dto.ParsedResume;
import com.example.resume_parser.entity.Resume;
import com.example.resume_parser.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    private final ResumeParsingService parsingService;
    private final ResumeRepository repository;

    public ResumeService(ResumeParsingService parsingService, ResumeRepository repository) {
        this.parsingService = parsingService;
        this.repository = repository;
    }

    public Resume analyseAndSave(String fileName, String rawText) {
        ParsedResume parsed = parsingService.parse(rawText);

        Resume resume = new Resume();
        resume.setFileName(fileName);
        resume.setRawText(rawText);
        resume.setName(parsed.name());
        resume.setEmail(parsed.email());
        resume.setPhone(parsed.phone());
        resume.setSkills(join(parsed.skills(), ", "));
        resume.setEducation(join(parsed.education(), "\n"));
        resume.setExperience(join(parsed.experience(), "\n"));

        return repository.save(resume);
    }

    private String join(List<String> items, String separator) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return String.join(separator, items);
    }
}
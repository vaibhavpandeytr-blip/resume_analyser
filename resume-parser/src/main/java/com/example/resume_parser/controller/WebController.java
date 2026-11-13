package com.example.resume_parser.controller;

import com.example.resume_parser.dto.MatchResult;
import com.example.resume_parser.entity.JobDescription;
import com.example.resume_parser.repository.JobDescriptionRepository;
import com.example.resume_parser.repository.ResumeRepository;
import com.example.resume_parser.service.SubmissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class WebController {

    private final SubmissionService submissionService;
    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobRepository;

    public WebController(SubmissionService submissionService,
                         ResumeRepository resumeRepository,
                         JobDescriptionRepository jobRepository) {
        this.submissionService = submissionService;
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
    }

    private JobDescription currentJob() {
        return jobRepository.findById(1L).orElseGet(() -> {
            JobDescription j = new JobDescription();
            j.setTitle("Software Developer");
            j.setDescription("Java, Spring Boot, PostgreSQL, REST APIs, Git");
            return jobRepository.save(j);
        });
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam("email") String email,
                         Model model) {
        try {
            JobDescription job = currentJob();
            MatchResult result = submissionService.submit(file, email, job.getDescription());

            model.addAttribute("result", result);
            model.addAttribute("resume", resumeRepository.findById(result.resumeId()).orElse(null));
            model.addAttribute("job", job);
            model.addAttribute("email", email.trim().toLowerCase());
            model.addAttribute("count", submissionService.history(email).size());
            return "report";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/history")
    public String history(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email.trim().toLowerCase());
        model.addAttribute("list", submissionService.history(email));
        return "history";
    }

    @GetMapping("/job")
    public String job(Model model) {
        model.addAttribute("job", currentJob());
        return "job";
    }

    @PostMapping("/job")
    public String saveJob(@RequestParam String title, @RequestParam String description) {
        JobDescription j = currentJob();
        j.setTitle(title);
        j.setDescription(description);
        jobRepository.save(j);
        return "redirect:/job";
    }
}
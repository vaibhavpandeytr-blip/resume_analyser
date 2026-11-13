package com.example.resume_parser.repository;

import com.example.resume_parser.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByEmailOrderBySubmittedAtDesc(String email);

    long countByEmail(String email);
}
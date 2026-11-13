package com.example.resume_parser.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_description")
public class JobDescription {

    @Id
    private Long id = 1L;   // always one row

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
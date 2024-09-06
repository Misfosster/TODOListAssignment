package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDate deadline;

    private String category;

    public enum TaskStatus {
        PENDING,
        COMPLETED
    }

    // Constructors
    public Task() {
    }

    public Task(String title, String description, LocalDate deadline, String category) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.deadline = deadline;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
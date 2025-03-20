package com.example.constructionsystem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Task {
    private Integer id_task ;
    private String description;
    private LocalDate deadline;

    public Task(Integer id, String description, LocalDate deadline) {
        this.id_task = id;
        this.description = description;
        this.deadline = deadline;
    }

    public Integer getId_task() {
        return id_task;
    }

    public void setId_task(Integer id_task) {
        this.id_task = id_task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}


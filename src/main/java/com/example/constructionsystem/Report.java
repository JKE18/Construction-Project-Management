package com.example.constructionsystem;

import java.time.LocalDate;

public class Report {
    private Integer id_report;
    private String content;
    private LocalDate date;

    public Report(Integer id, String content, LocalDate date) {
        this.id_report = id;
        this.content = content;
        this.date = date;
    }

    public Integer getId() {
        return id_report;
    }

    public void setId_report(int id_report) {
        this.id_report = id_report;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
}

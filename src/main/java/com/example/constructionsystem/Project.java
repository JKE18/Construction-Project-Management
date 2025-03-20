package com.example.constructionsystem;

import java.time.LocalDate;
import java.util.Date;

public class Project {

    private Integer id_project ;
    private String nazwa;
    private String klient;
    private String miejscowosc;
    private LocalDate deadline;
    private String etap;
    private String kosztorys;
    private String oplacona;
    private String opis;

    public Project(Integer id_project, String nazwa, String klient, String miejscowosc, LocalDate deadline, String etap, String kosztorys, String oplacona, String opis) {
        this.id_project = id_project;
        this.nazwa = nazwa;
        this.klient = klient;
        this.miejscowosc = miejscowosc;
        this.deadline = deadline;
        this.etap = etap;
        this.kosztorys = kosztorys;
        this.oplacona = oplacona;
        this.opis = opis;
    }

    public Project(int id, String nazwa, String kosztorys, String oplacona) {
        this.id_project = id;
        this.nazwa = nazwa;
        this.kosztorys = kosztorys;
        this.oplacona = oplacona;
    }

    public Integer getProjectID() {
        return id_project;
    }
    public void setProjectID(Integer id) {
        this.id_project = id;
    }
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKlient() {
        return klient;
    }

    public void setKlient(String klient) {
        this.klient = klient;
    }

    public String getMiejscowosc() {
        return miejscowosc;
    }

    public void setMiejscowosc(String miejscowosc) {
        this.miejscowosc = miejscowosc;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getEtap() {
        return etap;
    }

    public void setEtap(String etap) {
        this.etap = etap;
    }
    public String getKosztorys() {
        return kosztorys;
    }

    public void setKosztorys(String kosztorys) {
        this.kosztorys = kosztorys;
    }

    public String getOplacona() {
        return oplacona;
    }

    public void setOplacona(String oplacona) {
        this.oplacona = oplacona;
    }
    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}

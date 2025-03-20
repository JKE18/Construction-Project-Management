package com.example.constructionsystem;



public class Employee {

    private Integer id_employee ;
    private String imie;
    private String nazwisko;
    private String stan_pracownika;
    private Integer rok_urodzenia;
    private String wynagrodzenie;
    private String email;
    private String phone;
    private String stanowisko;


    public Employee(Integer employeeID,String imie, String nazwisko, String stan_pracownika, int rok_urodzenia, String wynagrodzenie, String email, String phone, String stanowisko) {
        this.id_employee =employeeID;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.stan_pracownika = stan_pracownika;
        this.rok_urodzenia = rok_urodzenia;
        this.wynagrodzenie = wynagrodzenie;
        this.email = email;
        this.phone = phone;
        this.stanowisko = stanowisko;
    }

    public Integer getEmployeeid() {
        return id_employee ;
    }
    public String getImie() {
        return imie;
    }
    public String getNazwisko() {
        return nazwisko;
    }
    public String getStan_pracownika() {
        return stan_pracownika;
    }
    public int getRok_urodzenia() {
        return rok_urodzenia;
    }
    public String getWynagrodzenie() {
        return wynagrodzenie;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getStanowisko() {
        return stanowisko;
    }

    public void setEmployeeID(Integer employeeID) {this.id_employee  = employeeID;}
    public void setImie(String imie) {
        this.imie = imie;
    }
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    public void setStan_pracownika(String stan_pracownika) {this.stan_pracownika = stan_pracownika;}
    public void setRok_urodzenia(int rok_urodzenia) {
        this.rok_urodzenia = rok_urodzenia;
    }
    public void setWynagrodzenie(String wynagrodzenie) {
        this.wynagrodzenie = wynagrodzenie;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }
}
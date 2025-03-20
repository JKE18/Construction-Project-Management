package com.example.constructionsystem;

public class StawkiRemont {
    private double tynkowanieScian;
    private double malowanieITapetowanie;
    private double szpachlowanieScian;
    private double murowanieScian;
    private double montazPlytGK;
    private double ukladaniePaneliIParkietu;
    private double praceInstalacyjne;
    private double bialyMontaz;
    private double montazDrzwiWewnetrznych;
    private double montazOkien;
    private double ukladaniePlytek;
    private double pozostaleKosztyDodatkowe;

    public StawkiRemont(double tynkowanieScian, double malowanieITapetowanie, double szpachlowanieScian, double murowanieScian, double montazPlytGK, double ukladaniePaneliIParkietu, double praceInstalacyjne, double bialyMontaz, double montazDrzwiWewnetrznych, double montazOkien, double ukladaniePlytek, double pozostaleKosztyDodatkowe) {
        this.tynkowanieScian = tynkowanieScian;
        this.malowanieITapetowanie = malowanieITapetowanie;
        this.szpachlowanieScian = szpachlowanieScian;
        this.murowanieScian = murowanieScian;
        this.montazPlytGK = montazPlytGK;
        this.ukladaniePaneliIParkietu = ukladaniePaneliIParkietu;
        this.praceInstalacyjne = praceInstalacyjne;
        this.bialyMontaz = bialyMontaz;
        this.montazDrzwiWewnetrznych = montazDrzwiWewnetrznych;
        this.montazOkien = montazOkien;
        this.ukladaniePlytek = ukladaniePlytek;
        this.pozostaleKosztyDodatkowe = pozostaleKosztyDodatkowe;
    }

    // Gettery i settery
    public double getTynkowanieScian() { return tynkowanieScian; }
    public void setTynkowanieScian(double tynkowanieScian) { this.tynkowanieScian = tynkowanieScian; }

    public double getMalowanieITapetowanie() { return malowanieITapetowanie; }
    public void setMalowanieITapetowanie(double malowanieITapetowanie) { this.malowanieITapetowanie = malowanieITapetowanie; }

    public double getSzpachlowanieScian() { return szpachlowanieScian; }
    public void setSzpachlowanieScian(double szpachlowanieScian) { this.szpachlowanieScian = szpachlowanieScian; }

    public double getMurowanieScian() { return murowanieScian; }
    public void setMurowanieScian(double murowanieScian) { this.murowanieScian = murowanieScian; }

    public double getMontazPlytGK() { return montazPlytGK; }
    public void setMontazPlytGK(double montazPlytGK) { this.montazPlytGK = montazPlytGK; }

    public double getUkladaniePaneliIParkietu() { return ukladaniePaneliIParkietu; }
    public void setUkladaniePaneliIParkietu(double ukladaniePaneliIParkietu) { this.ukladaniePaneliIParkietu = ukladaniePaneliIParkietu; }

    public double getPraceInstalacyjne() { return praceInstalacyjne; }
    public void setPraceInstalacyjne(double praceInstalacyjne) { this.praceInstalacyjne = praceInstalacyjne; }

    public double getBialyMontaz() { return bialyMontaz; }
    public void setBialyMontaz(double bialyMontaz) { this.bialyMontaz = bialyMontaz; }

    public double getMontazDrzwiWewnetrznych() { return montazDrzwiWewnetrznych; }
    public void setMontazDrzwiWewnetrznych(double montazDrzwiWewnetrznych) { this.montazDrzwiWewnetrznych = montazDrzwiWewnetrznych; }

    public double getMontazOkien() { return montazOkien; }
    public void setMontazOkien(double montazOkien) { this.montazOkien = montazOkien; }

    public double getUkladaniePlytek() { return ukladaniePlytek; }
    public void setUkladaniePlytek(double ukladaniePlytek) { this.ukladaniePlytek = ukladaniePlytek; }

    public double getPozostaleKosztyDodatkowe() { return pozostaleKosztyDodatkowe; }
    public void setPozostaleKosztyDodatkowe(double pozostaleKosztyDodatkowe) { this.pozostaleKosztyDodatkowe = pozostaleKosztyDodatkowe; }
}

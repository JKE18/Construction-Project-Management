package com.example.constructionsystem;

public class StawkiDom {
    private double zalewanieFundamentow;
    private double hydroizolacjaFundamentow;
    private double murowanieScianNosnych;
    private double murowanieScianDzialowych;
    private double zbrojenieStalowe;
    private double stropISchody;
    private double montazWiezbyDachowej;
    private double pokrycieDachowe;
    private double kominyIWentylacja;
    private double montazOkienDachowych;
    private double elewacja;
    private double pozostaleKosztyDodatkowe;

    public StawkiDom(double zalewanieFundamentow, double hydroizolacjaFundamentow, double murowanieScianNosnych, double murowanieScianDzialowych, double zbrojenieStalowe, double stropISchody, double montazWiezbyDachowej, double pokrycieDachowe, double kominyIWentylacja, double montazOkienDachowych, double elewacja, double pozostaleKosztyDodatkowe) {
        this.zalewanieFundamentow = zalewanieFundamentow;
        this.hydroizolacjaFundamentow = hydroizolacjaFundamentow;
        this.murowanieScianNosnych = murowanieScianNosnych;
        this.murowanieScianDzialowych = murowanieScianDzialowych;
        this.zbrojenieStalowe = zbrojenieStalowe;
        this.stropISchody = stropISchody;
        this.montazWiezbyDachowej = montazWiezbyDachowej;
        this.pokrycieDachowe = pokrycieDachowe;
        this.kominyIWentylacja = kominyIWentylacja;
        this.montazOkienDachowych = montazOkienDachowych;
        this.elewacja = elewacja;
        this.pozostaleKosztyDodatkowe = pozostaleKosztyDodatkowe;
    }

    // Gettery i settery
    public double getZalewanieFundamentow() { return zalewanieFundamentow; }
    public void setZalewanieFundamentow(double zalewanieFundamentow) { this.zalewanieFundamentow = zalewanieFundamentow; }

    public double getHydroizolacjaFundamentow() { return hydroizolacjaFundamentow; }
    public void setHydroizolacjaFundamentow(double hydroizolacjaFundamentow) { this.hydroizolacjaFundamentow = hydroizolacjaFundamentow; }

    public double getMurowanieScianNosnych() { return murowanieScianNosnych; }
    public void setMurowanieScianNosnych(double murowanieScianNosnych) { this.murowanieScianNosnych = murowanieScianNosnych; }

    public double getMurowanieScianDzialowych() { return murowanieScianDzialowych; }
    public void setMurowanieScianDzialowych(double murowanieScianDzialowych) { this.murowanieScianDzialowych = murowanieScianDzialowych; }

    public double getZbrojenieStalowe() { return zbrojenieStalowe; }
    public void setZbrojenieStalowe(double zbrojenieStalowe) { this.zbrojenieStalowe = zbrojenieStalowe; }

    public double getStropISchody() { return stropISchody; }
    public void setStropISchody(double stropISchody) { this.stropISchody = stropISchody; }

    public double getMontazWiezbyDachowej() { return montazWiezbyDachowej; }
    public void setMontazWiezbyDachowej(double montazWiezbyDachowej) { this.montazWiezbyDachowej = montazWiezbyDachowej; }

    public double getPokrycieDachowe() { return pokrycieDachowe; }
    public void setPokrycieDachowe(double pokrycieDachowe) { this.pokrycieDachowe = pokrycieDachowe; }

    public double getKominyIWentylacja() { return kominyIWentylacja; }
    public void setKominyIWentylacja(double kominyIWentylacja) { this.kominyIWentylacja = kominyIWentylacja; }

    public double getMontazOkienDachowych() { return montazOkienDachowych; }
    public void setMontazOkienDachowych(double montazOkienDachowych) { this.montazOkienDachowych = montazOkienDachowych; }

    public double getElewacja() { return elewacja; }
    public void setElewacja(double elewacja) { this.elewacja = elewacja; }

    public double getPozostaleKosztyDodatkowe() { return pozostaleKosztyDodatkowe; }
    public void setPozostaleKosztyDodatkowe(double pozostaleKosztyDodatkowe) { this.pozostaleKosztyDodatkowe = pozostaleKosztyDodatkowe; }
}

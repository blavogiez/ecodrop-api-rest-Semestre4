package model;

public class Partie {
    int pno;
    int jno1;
    int jno2;
    String date;
    String statut;
    int temps;
    int gagnant;

    public Partie(int pno, int jno1, int jno2, String date, String statut, int temps, int gagnant) {
        this.pno = pno;
        this.jno1 = jno1;
        this.jno2 = jno2;
        this.date = date;
        this.statut = statut;
        this.temps = temps;
        this.gagnant = gagnant;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public int getJno1() {
        return jno1;
    }

    public void setJno1(int jno1) {
        this.jno1 = jno1;
    }

    public int getJno2() {
        return jno2;
    }

    public void setJno2(int jno2) {
        this.jno2 = jno2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getGagnant() {
        return gagnant;
    }

    public void setGagnant(int gagnant) {
        this.gagnant = gagnant;
    }

    @Override
    public String toString() {
        return "Partie [pno=" + pno + ", jno1=" + jno1 + ", jno2=" + jno2 + ", date=" + date + ", statut=" + statut
                + ", temps=" + temps + ", gagnant=" + gagnant + "]";
    }
}

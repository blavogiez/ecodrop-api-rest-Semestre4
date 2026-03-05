package model ;

public class Joueur {
//     create table joueur
// ( jno int primary key,
//   pseudo text,
//   email text,
//   pwd text,
//   elo int);
    private int jno ;
    private String pseudo ;
    private String email ;
    private String pwd ;
    private int elo ;

    public Joueur(int jno, String pseudo, String email, String pwd, int elo) {
        this.jno = jno;
        this.pseudo = pseudo;
        this.email = email;
        this.pwd = pwd;
        this.elo = elo;
    }

    public int getJno() {
        return jno;
    }
    public void setJno(int jno) {
        this.jno = jno;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public int getElo() {
        return elo;
    }
    public void setElo(int elo) {
        this.elo = elo;
    }

    @Override
    public String toString() {
        return "Joueur [jno=" + jno + ", pseudo=" + pseudo + ", email=" + email + ", elo=" + elo + "]";
    }

    
    
    
}

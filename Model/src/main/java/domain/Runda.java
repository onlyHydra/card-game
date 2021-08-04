package domain;

import java.io.Serializable;

public class Runda extends Entity<Integer> implements Serializable {
    private int nrRunda;
    private int idJoc;
    private String username;
    private String carteAleasa;
    private int nrCartiCastigate;

    public Runda() {
    }

    public Runda(int nrRunda, int idJoc, String username, String carteAleasa, int nrCartiPrimite) {
        this.nrRunda = nrRunda;
        this.idJoc = idJoc;
        this.username = username;
        this.carteAleasa = carteAleasa;
        this.nrCartiCastigate = nrCartiPrimite;
    }

    public int getNrRunda() {
        return nrRunda;
    }

    public void setNrRunda(int nrRunda) {
        this.nrRunda = nrRunda;
    }

    public int getIdJoc() {
        return idJoc;
    }

    public void setIdJoc(int idJoc) {
        this.idJoc = idJoc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarteAleasa() {
        return carteAleasa;
    }

    public void setCarteAleasa(String carteAleasa) {
        this.carteAleasa = carteAleasa;
    }

    public int getNrCartiCastigate() {
        return nrCartiCastigate;
    }

    public void setNrCartiCastigate(int nrCartiPrimite) {
        this.nrCartiCastigate = nrCartiPrimite;
    }

    @Override
    public String toString() {
        return "Runda{" +
                ", nrRunda=" + nrRunda +
                ", idJoc=" + idJoc +
                ", username='" + username + '\'' +
                ", carteAleasa='" + carteAleasa + '\'' +
                ", nrCartiPrimite=" + nrCartiCastigate +
                '}';
    }
}

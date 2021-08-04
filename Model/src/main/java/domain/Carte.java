package domain;

import java.io.Serializable;

public class Carte implements Serializable {
    private String denumire;

    public Carte() {
    }

    public Carte(String denumire) {
        this.denumire = denumire;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    @Override
    public String toString() {
        return "Carte{" +
                "denumire='" + denumire + '\'' +
                '}';
    }
}

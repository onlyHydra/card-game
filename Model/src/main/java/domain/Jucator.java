package domain;

import java.io.Serializable;

public class Jucator extends Entity<String> implements Serializable {
    private String id;
    private String parola;
    public Jucator() {
    }

    public Jucator(String parola) {
        this.parola = parola;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String s) {
        this.id=s;
    }

    @Override
    public String toString() {
        return "Jucator{" +
                "id='" + id + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }

}

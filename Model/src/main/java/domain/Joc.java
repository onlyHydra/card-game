package domain;

import java.io.Serializable;

public class Joc extends Entity<Integer> implements Serializable {
    private String username;
    private String carte1;
    private String carte2;
    private String carte3;

    public Joc() {
    }

    public Joc(String username, String carte1, String carte2, String carte3) {
        this.username = username;
        this.carte1 = carte1;
        this.carte2 = carte2;
        this.carte3 = carte3;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCarte1() {
        return carte1;
    }

    public void setCarte1(String carte1) {
        this.carte1 = carte1;
    }

    public String getCarte2() {
        return carte2;
    }

    public void setCarte2(String carte2) {
        this.carte2 = carte2;
    }

    public String getCarte3() {
        return carte3;
    }

    @Override
    public String toString() {
        return "Joc{" +
                "username='" + username + '\'' +
                ", carte1='" + carte1 + '\'' +
                ", carte2='" + carte2 + '\'' +
                ", carte3='" + carte3 + '\'' +
                '}';
    }

    public void setCarte3(String carte3) {
        this.carte3 = carte3;
    }
}

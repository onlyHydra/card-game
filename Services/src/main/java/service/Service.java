package service;

import domain.Jucator;

public interface Service {
    public void login(Jucator user, Observer client);
    void logout(Jucator user, Observer client);
    public void start(Jucator player);
    public void joacaRunda(Jucator player, String carte);
}

package dto;

import domain.Carte;

import java.io.Serializable;
import java.util.List;

public class DTOStart implements Serializable {
    private String readyToPlay;
    private String cards;

    public String getReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(String readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    public String getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "DTOStart{" +
                "readyToPlay=" + readyToPlay +
                ", cards=" + cards +
                '}';
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public DTOStart(String readyToPlay, String cards) {
        this.readyToPlay = readyToPlay;
        this.cards = cards;
    }
}

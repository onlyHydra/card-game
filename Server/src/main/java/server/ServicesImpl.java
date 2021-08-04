package server;

import domain.Carte;
import domain.Joc;
import domain.Jucator;
import domain.Runda;
import rosu.persistence.JocRepository;
import rosu.persistence.JucatorRepository;
import rosu.persistence.RundaRepository;
import service.Observer;
import service.RosuException;
import service.Service;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements Service {
    private JocRepository jocRepository;
    private RundaRepository rundaRepository;
    private JucatorRepository jucatorRepository;
    private Map<String, Observer> loggedClients;
    private List<String> allCards = new ArrayList<>();
    private List<String> readyToPlay = new ArrayList<>(); //players that clicked "play"
    private boolean hasStarted = false;
    private int gameId;
    private int roundNr=1;
    private Map<String, String> choices = new HashMap<>(); //players that picked a card





    public ServicesImpl(JocRepository jocRepository, RundaRepository rundaRepository, JucatorRepository jucatorRepository) {
        this.jocRepository=jocRepository;
        this.rundaRepository=rundaRepository;
        this.jucatorRepository = jucatorRepository;
        loggedClients=new ConcurrentHashMap<>();
        addAllCards();
        gameId=getNextGameId();
    }

    private void addAllCards() {
        allCards.add("red.8");
        allCards.add("red.9");
        allCards.add("red.10");
        allCards.add("black.8");
        allCards.add("black.5");
        allCards.add("black.2");
        allCards.add("black.7");
        allCards.add("red.7");
        allCards.add("red.2");
    }

    @Override
    public void login(Jucator user, Observer client){
        Jucator jucator = jucatorRepository.findOne(user.getId());
        if(jucator==null)
            throw new RosuException("Niciun jucator cu acest username!");
        else if(loggedClients.get(user.getId()) != null)
            throw new RosuException("Jucator deja logat!");
        else if(!jucator.getParola().equals(user.getParola()))
            throw new RosuException("Parola gresita!");

        else {
            loggedClients.put(jucator.getId(), client);
        }
    }

    @Override
    public void logout(Jucator user, Observer client) {
        Observer localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new RosuException("User "+user.getId()+" is not logged in.");
    }

    public List<Jucator> getLoggedPlayers() {
        List<Jucator> jucatori=new ArrayList<>();
        for (Jucator jucator : jucatorRepository.findAll()){
            if (loggedClients.containsKey(jucator.getId())){//the player is logged in
                jucatori.add(jucator);
            }
        }
        System.out.println("Size "+jucatori.size());
        return jucatori;
    }

    private final int defaultThreadsNo=5;
    private void notifyNewGame(String jucator, List<Carte> cards) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(String str : loggedClients.keySet()){
            //System.out.println(str);
            if(readyToPlay.indexOf(str)!=-1 && str.equals(jucator)) {
                Observer iObserver = loggedClients.get(str);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + str + " new game");
                            iObserver.notifyNewGame(readyToPlay, cards);
                        } catch (RemoteException | RosuException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }

    public void start(Jucator player){
        if(readyToPlay.indexOf(player.getId())==-1 && !hasStarted)
            readyToPlay.add(player.getId());
        if(readyToPlay.size()==3) {
            hasStarted = true;
            for(String jucator: readyToPlay) {
                List<Carte> ownCards = new ArrayList<>();
                Joc joc = new Joc();
                joc.setId(gameId);
                joc.setUsername(jucator);
                for(int i=0;i<3;i++){
                    Random rand = new Random();
                    String card = allCards.get(rand.nextInt(allCards.size()));
                    ownCards.add(new Carte(card));
                    allCards.remove(card);
                }
                joc.setCarte1(ownCards.get(0).getDenumire());
                joc.setCarte2(ownCards.get(1).getDenumire());
                joc.setCarte3(ownCards.get(2).getDenumire());
                jocRepository.save(joc);
                notifyNewGame(jucator,ownCards);
            }
        }
    }

    @Override
    public void joacaRunda(Jucator player, String carte){
        choices.put(player.getId(),carte);
        if(choices.keySet().size()==3) {
            String primite="";
            String cine="";
            int redCount=0;
            for (String s: choices.keySet()) {
                primite = primite + s + " a trimis cartea " + choices.get(s) + "\n";
                if(choices.get(s).contains("red"))
                    redCount+=1;
            }

            if(redCount == 0) { //smallest card wins
                int mn=9;

                for (String s: choices.keySet()) {
                    String ultima = choices.get(s).substring(choices.get(s).length() - 1);
                    if(Integer.parseInt(ultima)<mn) {
                        cine = s;
                        mn = Integer.parseInt(ultima);
                    }

                }
                primite = primite + cine + " a luat cartile";
            }
            else if (redCount==1){//user with red card wins
                for (String s: choices.keySet())
                    if(choices.get(s).contains("red")){
                        cine=s;
                        primite=primite+ s+ " a luat cartile";
                        break;
                    }
            }
            else{ //user with biggest red card wins
                int mx=0;

                for (String s: choices.keySet()) {
                    String ultima = choices.get(s).substring(choices.get(s).length() - 1);
                    if (choices.get(s).contains("red") && Integer.parseInt(ultima)>mx) {
                        cine=s;
                        mx=Integer.parseInt(ultima);
                    }
                }
                primite=primite+ cine+ " a luat cartile";

            }
            for (String s: choices.keySet()){
                Runda runda = new Runda();
                runda.setNrRunda(roundNr);
                runda.setCarteAleasa(choices.get(s));
                runda.setIdJoc(gameId);
                runda.setUsername(s);
                if(s.compareTo(cine)==0)
                    runda.setNrCartiCastigate(3);
                else runda.setNrCartiCastigate(0);
                rundaRepository.save(runda);
            }

            notifyWinner(primite);
            choices.clear();
            if(roundNr%3==0) {//game is over
                Map<String, Integer> rezultate=new HashMap<>();
                for(String str: loggedClients.keySet()){
                    if(readyToPlay.indexOf(str)!=-1){
                        rezultate.put(str,rundaRepository.nrCartiCastigate(gameId,str));
                    }
                }
                rezultate.entrySet() //sort results
                        .stream()
                        .sorted(Map.Entry.comparingByValue());
                String s="";
                for(String c:rezultate.keySet()){
                    s+=c+ " a castigat in total " +rezultate.get(c) + " carti\n";
                }
                notifyEndGame(s);
                roundNr = 1;
                gameId=getNextGameId();

            }
            else
                roundNr+=1;
        }

    }

    private void notifyEndGame(String s){
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(String str: loggedClients.keySet()){
            if(readyToPlay.indexOf(str)!=-1){
                Observer iObserver = loggedClients.get(str);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + str + "end game");
                            iObserver.notifyEndGame(s);
                        } catch (RemoteException | RosuException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }


    }

    private int getNextGameId(){
        int maxId=0;
        Iterable<Joc> games = jocRepository.findAll();
        for (Joc joc: games)
            if(joc.getId()>maxId)
                maxId = joc.getId();
        gameId=maxId+1;
        return maxId+1;
    }

    private void notifyWinner(String a) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(String str : loggedClients.keySet()){
            if(readyToPlay.indexOf(str)!=-1) {
                Observer iObserver = loggedClients.get(str);
                if (iObserver != null)
                    executor.execute(() -> {
                        try {
                            System.out.println("Notifying " + str + " new winner");
                            iObserver.notifyWinner(a);
                        } catch (RemoteException | RosuException e) {
                            System.err.println("Error notifying " + e);
                        }
                    });
            }
        }
        executor.shutdown();
    }




}

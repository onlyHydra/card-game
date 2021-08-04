package gui;

import domain.Carte;
import domain.Jucator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.Observer;
import service.RosuException;
import service.Service;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MainController extends UnicastRemoteObject implements Observer, Serializable {
    private Service service;
    private Jucator crtJucator;
    ObservableList<Carte> model = FXCollections.observableArrayList();
    private List<Carte> carti = new ArrayList<>();
    private Carte lastChoice;


    @FXML
    TextArea textAreaEndGame;
    @FXML
    TextArea textArea;
    @FXML
    Button button;
    @FXML
    Button butonAlege;
    @FXML
    Button btnStart;
    @FXML
    ListView<String> listViewOpponents;
    @FXML
    TableView<Carte> tableViewCards;
    @FXML
    TableColumn<Carte, String> tableColumnDenumire;

    public MainController() throws RemoteException {
    }


    @FXML
    public void initialize() {
        tableColumnDenumire.setCellValueFactory(new PropertyValueFactory<Carte, String>("denumire"));
        tableViewCards.setItems(model);
    }


    @FXML
    public void carteAleasa(){
        try {
            Carte carte = tableViewCards.getSelectionModel().getSelectedItem();
            if (carte != null) {
                service.joacaRunda(crtJucator, carte.getDenumire());
                butonAlege.setDisable(true);
                lastChoice = carte;
            } else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Error!", "Va rog alegeti o carte!");


        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Error!", e.getMessage());
        }

    }

    @FXML
    public void startJoc() {
        try {
            service.start(this.crtJucator);
            btnStart.setDisable(true);
        } catch (RosuException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Start failed!", e.getMessage());
        }
    }


    @FXML
    public void initModel() {

    }



    @FXML
    public void logOut(){
        try {

            service.logout(crtJucator, this);
            Stage stage = (Stage)button.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/logInView.fxml"));
            Parent root = loader.load();

            LogInController logInController = loader.getController();
            logInController.setService(service);

            FXMLLoader cloader = new FXMLLoader();
            cloader.setLocation(getClass().getResource("/views/mainView.fxml"));
            Parent croot=cloader.load();


            MainController chatCtrl =
                    cloader.<MainController>getController();
            chatCtrl.setService(service);

            logInController.setMainController(chatCtrl);
            logInController.setParent(croot);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log in:");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.show();

            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (RosuException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Iesire esuata!", e.getMessage());

        }
    }

    public void setService(Service service) {
        this.service=service;
        listViewOpponents.setDisable(true);
        butonAlege.setDisable(true);


    }
    public void setUser(Jucator user) {
        this.crtJucator = user;
    }

    @Override
    public void notifyNewGame(List<String> readyToPlay, List<Carte> cards) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                carti=cards;
                if (listViewOpponents.getItems().size() > 0)
                    listViewOpponents.getItems().clear();
                listViewOpponents.getItems().addAll(readyToPlay);
                listViewOpponents.getItems().remove(crtJucator.getId());
                model.addAll(cards);
                tableViewCards.setItems(model);
                butonAlege.setDisable(false);
                butonAlege.setDisable(false);
            }
        });

    }

    @Override
    public void notifyWinner(String a) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textArea.clear();
                carti.remove(lastChoice);
                model.clear();
                model.addAll(carti);
                tableViewCards.setItems(model);
                textArea.setText(a);
                butonAlege.setDisable(false);
            }
        });

    }

    @Override
    public void notifyEndGame(String a) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textAreaEndGame.clear();
                textAreaEndGame.setText(a);
                butonAlege.setDisable(true);
                btnStart.setDisable(true);
            }
        });
    }
}
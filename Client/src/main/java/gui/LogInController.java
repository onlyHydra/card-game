package gui;

import domain.Jucator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.RosuException;
import service.Service;

public class LogInController {
    private Service service;
    private Jucator crtJucator;

    MainController mainCtrl;

    Parent mainParent;

    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldParola;
    @FXML
    Button button;

    @FXML
    private void logIn(ActionEvent actionEvent) {

        String username = textFieldUsername.getText().toString();
        String passwd = textFieldParola.getText().toString();
        crtJucator = new Jucator(passwd);
        crtJucator.setId(username);
        try {
            service.login(crtJucator, mainCtrl);
            Stage stage = new Stage();
            //stage.setTitle("Welcome back  " + crtAngajat.getUsername() + "!");
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainCtrl.logOut();
                    System.exit(0);
                }
            });

            stage.show();
            stage.setTitle(crtJucator.getId());
            mainCtrl.setService(service);
            mainCtrl.setUser(crtJucator);
            mainCtrl.initModel();

            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

        } catch (RosuException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Talent Show");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }


    }

    public void setParent(Parent p) {
        mainParent = p;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setMainController(MainController chatController) {
        this.mainCtrl = chatController;
    }

    @FXML
    private void initialize() {
    }
}
import gui.LogInController;
import gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.Service;
import org.springframework.context.ApplicationContext;


import java.io.IOException;
import java.util.Properties;

public class StartRpcClient extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            Service server = (Service) factory.getBean("service");
            System.out.println("Obtained a reference to remote  server");


            //login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/logInView.fxml"));
            Parent root = loader.load();
            LogInController ctrl = loader.getController();
            ctrl.setService(server);


            FXMLLoader bLoader = new FXMLLoader(getClass().getResource("views/mainView.fxml"));
            Parent bRoot = bLoader.load();
            MainController mainController = bLoader.getController();
            mainController.setService(server);
            ctrl.setMainController(mainController);
            ctrl.setParent(bRoot);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error while starting the app" + ex);
            alert.showAndWait();


        }
    }
}

package chat;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene (root);
        primaryStage.setTitle("Clavardage 1.1");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(windowEvent -> Controller.getInstance().stop());
        //primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream( "icon.png" )));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

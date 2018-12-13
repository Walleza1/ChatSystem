package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Main extends Application implements Observer {
    public int nb_message=0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("root.fxml"));
        primaryStage.setTitle("Clavardage 1.0");
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        scene.setRoot(FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable observable, Object o) {
        this.nb_message+=1;
        System.out.println("Nombre message "+this.nb_message);
    }

}

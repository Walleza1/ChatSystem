package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{



    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene (root);
        primaryStage.setTitle("Clavardage 1.1");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(windowEvent -> Controller.getInstance().stop());
        Image image = new Image("/icon2.png");
        primaryStage.getIcons().add(image);
        primaryStage.show();
        Controller.getInstance().setStage(primaryStage);
    }

    @Override
    public void stop(){
        Controller.getInstance().logout();


    }

    public static void main(String[] args) {
        launch(args);
    }

}

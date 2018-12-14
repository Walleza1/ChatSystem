package chat.view;

import chat.Controller;
import chat.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    Controller controller = Controller.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList.getItems().add("Ceci est un test");
        userList.getItems().add("Ceci est un autre test djhefjehsdkgjdhfcglfkjhdglkjfhgk");
        username.setText(controller.getUsername());
    }

    @FXML
    private Button logout;

    @FXML
    private Label username;

    @FXML
    private ImageView changeUsernameIcon;

    @FXML
    private ListView userList;

    @FXML
    public void logOut (MouseEvent event) throws IOException {
        //call controller method Delete all data
        Parent chat_parent = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene chat_scene = new Scene(chat_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(chat_scene);
        app_stage.show();
    }

    @FXML
    public void changeUsername () {
        System.out.println("Coucou");
        username.setText("");
    }

}

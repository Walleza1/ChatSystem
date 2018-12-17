package chat.view;

import chat.Controller;
import chat.Main;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    Controller controller = Controller.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userList.setStyle("-fx-background-insets: 0 ;");
        userList.setStyle("-fx-control-inner-background: #242A31;");
        userList.setFixedCellSize(50);
        userList.getItems().add("Jean");
        userList.getItems().add("Petit");
        userList.getItems().add("Qui");
        userList.getItems().add("Danse");
        username.setText(controller.getUsername());
        distantUser.setOpacity(0);
    }

    @FXML
    private Button logout;

    @FXML
    private Label username;

    @FXML
    private Label distantUser;

    @FXML
    private ImageView changeUsernameIcon;

    @FXML
    private ListView userList;

    @FXML
    private TextField newUsername = null;

    @FXML
    private Button closeDiscussionButton;


    @FXML
    public void logOut (MouseEvent event) throws IOException {
        controller.logout();
        //call controller method Delete all data
        Parent chat_parent = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene chat_scene = new Scene(chat_parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(chat_scene);
        app_stage.show();
    }


    @FXML
    public void About () {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À-propos");
        alert.setHeaderText(null);
        alert.setContentText("Ce projet a été réalisé dans le cadre du projet POO 2018 par Jérôme Kompé et Vincent Erb à l'INSA Toulouse.");

        alert.showAndWait();
    }


    @FXML
    public void changeUsername () {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Changer de nom d'utilisateur");
        dialog.setHeaderText("Changement de nom d'utilisateur");
        dialog.setContentText("Entrer un nouveau nom d'utilisateur : ");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            if (controller.isUsernameAvailable(result.get())) {
                username.setText(result.get());
            }
        }
    }

    @FXML
    public void userClicked () {
        distantUser.setOpacity(1);
        distantUser.setText((String) userList.getSelectionModel().getSelectedItem());
    }
}

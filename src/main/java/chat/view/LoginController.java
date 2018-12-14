package chat.view;

import chat.Controller;
import chat.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Controller controller = Controller.getInstance();

    @FXML
    private Button test;

    @FXML
    private TextField userTextField;

    public LoginController(){}

    @FXML
    public void handleLoginClick(MouseEvent event) throws IOException{
        if (userTextField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez entrer un nom d'utilisateur.");
            alert.showAndWait();

        } else if (controller.isUsernameAvailable(userTextField.getText())){
            controller.setUsername(userTextField.getText());
            Parent chat_parent = FXMLLoader.load(getClass().getResource("/chat.fxml"));
            Scene chat_scene = new Scene(chat_parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(chat_scene);
            app_stage.show();
        }


    }
    @FXML
    public void handleHoverConnect(){
        test.setStyle("-fx-background-color: #79f4b3;");
    }

    @FXML
    public void handleHoverConnectDone(){
        test.setStyle("-fx-background-color:  #6ED9A0;");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}

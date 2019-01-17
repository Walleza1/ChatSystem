package chat.view;

import chat.Controller;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Button serverButton;

    @FXML
    private TextField userTextField;

    public LoginController(){}

    public void login (Event e) throws IOException{
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
            chat_scene.getStylesheets().add("/chatstyle.css");
            Stage app_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            app_stage.setScene(chat_scene);
            app_stage.show();
        } else {
            controller.getList().clear();
        }
    }

    @FXML
    public void handleLoginClick(MouseEvent event) throws IOException{
        login(event);
    }

    @FXML
    public void connectViaEnter (KeyEvent event) throws IOException{
        if(event.getCode().equals(KeyCode.ENTER)){
            login(event);
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

    @FXML
    public void servon (){
        if (serverButton.getText() == "Server on"){
            //eteint
            Controller.getInstance().initBroadcast();
            serverButton.setText("Server off");
        }else {
            //allumé
            Controller.getInstance().initServer();
            serverButton.setText("Server on");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}

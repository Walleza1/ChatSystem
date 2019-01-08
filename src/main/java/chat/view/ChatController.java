package chat.view;

import chat.Controller;
import chat.models.Message;
import chat.models.Notifications;
import chat.models.User;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatController implements Initializable, ListChangeListener, MapChangeListener {

    private Controller controller = Controller.getInstance();

    private User activeUser = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userListView.setStyle("-fx-background-insets: 0 ;");
        userListView.setStyle("-fx-control-inner-background: #242A31;");
        userListView.setFixedCellSize(50);
        username.setText(controller.getUsername());
        distantUser.setOpacity(0);
        textArea.setOpacity(0);
        closeDiscussionButton.setOpacity(0);
        fileButton.setOpacity(0);
        sendButton.setOpacity(0);
        controller.getList().addListener(this);
        controller.getMap().addListener(this);
        updateView();

    }

    private void updateView(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userListView.getItems().clear();
                for (User u : controller.getList()){
                    if (u != controller.getSelf()) {
                        userListView.getItems().add(u.getPseudo());
                    }
                }
            }
        });
    }

    private void updateFeed(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (activeUser != null) {
                    messageFeed.getItems().clear();
                    messageFeed.getItems().addAll(controller.getHistoryFromUser(activeUser));
                    messageFeed.scrollTo(messageFeed.getItems().size() - 1);
                }
            }
        });
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
    private ListView userListView;

    @FXML
    private TextField newUsername = null;

    @FXML
    private Button closeDiscussionButton;

    @FXML
    private Button fileButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView messageFeed;

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
        TextInputDialog dialog = new TextInputDialog(username.getText());
        dialog.setTitle("Changer de nom d'utilisateur");
        dialog.setHeaderText("Changement de nom d'utilisateur");
        dialog.setContentText("Entrer un nouveau nom d'utilisateur : ");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()){
            if (!controller.usernameInList(result.get())) {
                username.setText(result.get());
                controller.getSelf().setPseudo(result.get());

                for (User u : controller.getList()){
                    if(u.getAddress() == controller.getSelf().getAddress()){
                        u.setPseudo(result.get());
                    }
                }

                controller.sendPacket(Notifications.createNewPseudoPacket(controller.getSelf(),null));
                controller.setUsername(result.get());
                onChanged((ListChangeListener.Change) null);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce nom d'utilisateur est déjà pris");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void userClicked () {
        if (!userListView.getItems().isEmpty() && ((String) userListView.getSelectionModel().getSelectedItem() != null)){
            distantUser.setOpacity(1);
            textArea.setOpacity(1);
            closeDiscussionButton.setOpacity(1);
            fileButton.setOpacity(1);
            sendButton.setOpacity(1);
            distantUser.setText((String) userListView.getSelectionModel().getSelectedItem());
            activeUser = controller.getUserFromPseudo((String) userListView.getSelectionModel().getSelectedItem());
            updateFeed();
        }
    }

    @FXML
    public void closeDiscussion () {
        distantUser.setOpacity(0);
        textArea.setOpacity(0);
        closeDiscussionButton.setOpacity(0);
        fileButton.setOpacity(0);
        sendButton.setOpacity(0);
        messageFeed.getItems().clear();

    }

    @Override
    public void onChanged(ListChangeListener.Change c) {
        updateView();
    }

    @Override
    public void onChanged(MapChangeListener.Change change) {
        System.out.println("coucou");
        updateFeed();
    }

    @FXML
    public void sendByClick () {
        send();
    }

    @FXML
    public void sendByEnter (KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            event.consume();
            send();
        }
    }

    public void send (){
        //Prendre le texte et l'envoyer
        System.out.println(textArea.getText());
        Message toSend = new Message(0,controller.getSelf(),activeUser,textArea.getText());
        controller.getMessageListFromUser(activeUser).add(toSend);
        controller.sendPacket(toSend);
        textArea.clear();
        updateFeed();
    }


}

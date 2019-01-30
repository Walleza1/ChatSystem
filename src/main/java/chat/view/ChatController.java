package chat.view;

import chat.Controller;
import chat.models.Database;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ChatController implements Initializable, ListChangeListener, MapChangeListener {

    private Controller controller = Controller.getInstance();
    private Database db = Database.getInstance();

    private User activeUser = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userListView.setStyle("-fx-background-insets: 0 ;");
        userListView.setStyle("-fx-control-inner-background: #242A31;");
        userListView.setFixedCellSize(50);
        username.setText(controller.getUsername());
        hideDistant();
        controller.getList().addListener(this);
        controller.getMap().addListener(this);
        updateView();

    }

    private void hideDistant() {
        distantUser.setOpacity(0);
        textArea.setOpacity(0);
        closeDiscussionButton.setOpacity(0);
        fileButton.setOpacity(0);
        sendButton.setOpacity(0);
    }


    private void updateView(){
        Platform.runLater(() -> {
            userListView.getItems().clear();
            ArrayList<String> online = new ArrayList<>();
            ArrayList<String> offline = new ArrayList<>();

            for (User u : controller.getList()){
                if (!u.equals(controller.getSelf())) {
                    if(u.getStatus().equals(User.Status.offline)){
                        offline.add(u.getPseudo() + " - Hors ligne");
                    } else {
                        online.add(u.getPseudo());
                    }
                }
            }

            userListView.getItems().addAll(online);
            userListView.getItems().addAll(offline);

            if(activeUser != null) {
                boolean tmp = false ;
                for (User u : controller.getList()) {
                    if (u.getAddress() == activeUser.getAddress()) {
                        tmp = true;
                    }
                }

                if (!tmp) {
                    closeDiscussion();
                }
            }

            String s = online.size()+ "";
            usersOnline.setText(s);
        });
    }

    private void updateFeed(){
        Platform.runLater(() -> {
            if (activeUser != null) {
                messageFeed.getItems().clear();
                messageFeed.getItems().addAll(controller.getHistoryFromUser(activeUser));

                messageFeed.scrollTo(messageFeed.getItems().size() - 1);
            }
        });
    }

    @FXML
    private Label username;

    @FXML
    private Label distantUser;

    @FXML
    private ListView userListView;

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
    private Label usersOnline;

    @FXML
    public void logOut (MouseEvent event) throws IOException {
        controller.logout();
        //call controller method Delete all data
        Parent chat_parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
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
            if(result.get().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le nom d'utilisateur ne doit pas être vide");
                alert.showAndWait();
            }
            else if (!controller.usernameInList(result.get())) {
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
        if (!userListView.getItems().isEmpty() && (userListView.getSelectionModel().getSelectedItem() != null)){
            activeUser = controller.getUserFromPseudo(((String) userListView.getSelectionModel().getSelectedItem())
                    .replace(" - Hors ligne",""));
            if(activeUser.getStatus().equals(User.Status.online)){
                textArea.setDisable(false);
                fileButton.setDisable(false);
                sendButton.setDisable(false);
                fileButton.setOpacity(1);
                sendButton.setOpacity(1);
            } else {
                textArea.setDisable(true);
                fileButton.setDisable(true);
                sendButton.setDisable(true);
                fileButton.setOpacity(0);
                sendButton.setOpacity(0);
            }
            distantUser.setOpacity(1);
            textArea.setOpacity(1);
            closeDiscussionButton.setOpacity(1);
            distantUser.setText((String) userListView.getSelectionModel().getSelectedItem());
            updateFeed();
        }
    }

    @FXML
    public void closeDiscussion () {
        hideDistant();
        messageFeed.getItems().clear();
        textArea.setDisable(true);

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
    public void sendByEnter (KeyEvent event){
        if (event.getCode().equals(KeyCode.ENTER)) {
            event.consume();
            send();
        }
    }

    private void send(){
        //Prendre le texte et l'envoyer
        System.out.println(textArea.getText());
        Message toSend = new Message(controller.getSelf(),activeUser,textArea.getText());
        controller.getMessageListFromUser(activeUser).add(toSend);
        db.addMessage(controller.getSelf(),activeUser,toSend);
        controller.sendPacket(toSend);
        textArea.clear();
        updateFeed();
    }

    @FXML
    public void sendFile(MouseEvent event){
        System.out.println("hi");
        FileChooser choice = new FileChooser();
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = choice.showOpenDialog(app_stage);
        if(selectedFile != null){
            chat.models.File toSend = new chat.models.File(controller.getSelf(),
                    activeUser, null, selectedFile.getName());
            byte [] byte_file  = new byte [(int)selectedFile.length()];

            FileInputStream fis;
            try {
                fis = new FileInputStream(selectedFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                if(bis.read(byte_file,0,byte_file.length) == 1){
                    System.out.println("Couldn't write into byte_file");
                }
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            toSend.setContent(byte_file);
            controller.sendPacket(toSend);
            System.out.println("File sent");
        }

    }

}

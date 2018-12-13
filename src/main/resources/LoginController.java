import chat.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoginController {

    @FXML
    private Button test;

    private Main main;

    public LoginController(){}

    @FXML
    private void initialize(){}

    public void setMainApp(Main mainApp) {
        this.main = mainApp;
    }


}

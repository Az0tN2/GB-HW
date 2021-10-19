import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthWindow {
    Stage window;
    Client client;
    TextArea status;

    public AuthWindow(Client linkedClient) {
        this.client = linkedClient;
        window = new Stage();
        status = new TextArea();
        TextField log = new TextField();
        log.setPrefWidth(400);
        PasswordField pass = new PasswordField();
        pass.setPrefWidth(400);
        log.setPromptText("login");
        Button authButton = new Button();
        authButton.setText("Auth");
        authButton.setOnAction(e ->
        {
            client.sendMsg("/auth " + log.getText() + " " + pass.getText());
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(status, log, pass, authButton);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
    }

    public void show() {

        window.showAndWait();

    }

    public void setStatus(String text) {
        status.setText(text);
    }

    public void close() {
        window.close();
    }

}

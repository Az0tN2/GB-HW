import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ChatWindow extends Application {
    private final TextArea messages = new TextArea();
    private final Client client;
    Stage primaryStage;
    AuthWindow authWindow;

    public ChatWindow() {
        client = new Client();
        client.setWindow(this);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setMessages(String text) {
        messages.appendText(text);
    }

    private Parent createContent() {
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                client.sendMsg(input.getText());
                input.clear();
            }
        });
        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("test2");
        authWindow = new AuthWindow(client);
        authWindow.show();
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();



    }

    public void close() {
        Platform.exit();
    }

    public void setTitle(String nickname) {
        primaryStage.setTitle(nickname);
    }
}

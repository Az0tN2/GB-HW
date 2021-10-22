import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private final long timeOut = 2 * 60 * 1000;
    ChatWindow window;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private boolean authorized = false;

    public Client() {
        try {
            connect();

            createThread().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean auth) {
        authorized = auth;
    }

    public void connect() throws IOException {
        socket = new Socket("localhost", 8189);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        setAuthorized(false);
    }

    public void setWindow(ChatWindow window) {
        this.window = window;
    }

    public Thread createThread() {
        Thread thread = new Thread(() -> {
            try {
                long time = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - time >= timeOut) {
                        System.out.println("Out of time");
                        window.close();
                        return;
                    }
                    if (in.available() != 0) {
                        String str = in.readUTF();
                        if (str.startsWith("/authok")) {
                            setAuthorized(true);
                            Platform.runLater(() -> {
                                window.authWindow.close();
                            });

                            Platform.runLater(() -> {
                                window.setTitle(str.substring(8));
                            });
                            break;
                        } else if (str.equals("/Nickname is busy")) {
                            window.authWindow.setStatus("Nickname is busy!");
                        } else if (str.equals("/Wrong login/password")) {
                            window.authWindow.setStatus("Wrong login/password");
                        }
                        window.setMessages(str + "\n");
                    }

                }
                while (true) {
                    String str = in.readUTF();
                    if (str.equals("/end")) {
                        break;
                    }
                    window.setMessages(str + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setAuthorized(false);
            }
        });
        return thread;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            window.setMessages("Error!");
            e.printStackTrace();
        }
    }
}

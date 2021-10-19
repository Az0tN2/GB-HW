import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    MyServer myServer;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private String name;
    private boolean isAuthorised;

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    authorizeCycle();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    myServer.unsubscribe(this);
                    myServer.broadcastMsg(name + " вышел из чата");
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public String getName() {
        return name;
    }

    public void sendPrivateMessage(String str) {
        int spaceLocation = str.indexOf(" ", 3);
        String nameToSend = str.substring(3, spaceLocation);
        String message = str.substring(spaceLocation);
        myServer.broadcastMsg("Личка от " + name + ":" + message, nameToSend);
        DBWorker.saveMessage(name, nameToSend, message);
    }

    public void authorizeCycle() throws IOException {
        while (true) { // цикл авторизации
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick =
                        myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        myServer.subscribe(this);
                        isAuthorised = true;
                        break;
                    } else sendMsg("/Nickname is busy");
                } else {
                    sendMsg("/Wrong login/password");
                }
            }
        }

        while (isAuthorised) { // цикл получения сообщений
            String str = in.readUTF();
            if (str.startsWith("/chnick ")) {
                String[] token = str.split("\\s+", 2);
                if (token.length < 2) {
                    continue;
                }
                if (token[1].contains(" ")) {
                    sendMsg("Ник не может содержать пробелов");
                    continue;
                }
                if (myServer.getAuthService().changeNick(this.name, token[1])) {
                    sendMsg("/yournickis " + token[1]);
                    sendMsg("Ваш ник изменен на " + token[1]);
                    this.name = token[1];
                    myServer.broadcastClientList();
                } else {
                    sendMsg("Не удалось изменить ник. Ник " + token[1] + " уже существует");
                }
            }
            if (str.startsWith("/w")) {
                sendPrivateMessage(str);
                continue;
            }
            System.out.println("от " + name + ": " + str);
            if (str.equals("/end")) break;
            myServer.broadcastMsg(name + ": " + str);
            DBWorker.saveMessage(name, "all", str);
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
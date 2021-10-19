import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyServer {
    private final int PORT = 8189;
    private ServerSocket server;
    private final List<ClientHandler> clients;
    private final AuthService authService;

    public MyServer() {
        clients = new CopyOnWriteArrayList<>();
        if (!DBWorker.connect()) {
            throw new RuntimeException("DB Connection error");
        }
        authService = new DBAuthService();
        try {
            server = new ServerSocket(PORT);
            Socket socket = null;
            //authService = new BaseAuthService();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе сервера");
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getName().equals(nick)) return true;
        }
        return false;
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void broadcastMsg(String msg, String name) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(name)) {
                o.sendMsg(msg);
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientlist");
        for (ClientHandler c : clients) {
            sb.append(" ").append(c.getName());
        }

        String message = sb.toString();
        for (ClientHandler c : clients) {
            c.sendMsg(message);
        }
    }
}
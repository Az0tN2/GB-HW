public interface AuthService {

    String getNickByLoginPass(String login, String pass);
    boolean changeNick(String oldNick, String newNick);

}
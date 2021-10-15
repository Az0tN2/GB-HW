public class DBAuthService implements AuthService {

    @Override
    public String getNickByLoginPass(String login, String pass) {
       return DBWorker.getNickname(login, pass);
    }

    @Override
    public boolean changeNick(String oldNick, String newNick) {
        return DBWorker.changeNick(oldNick, newNick);
    }

}

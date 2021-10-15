import java.sql.*;

public class DBWorker {
    private static Connection connection;
    private static PreparedStatement getNickname;
    private static PreparedStatement changeNick;
    private static PreparedStatement saveMessage;

    public static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            prepareAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private static void prepareAll() throws SQLException{
        getNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
        changeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");
        saveMessage = connection.prepareStatement("INSERT INTO savedMessages (sender, receiver, message) VALUES (\n" + "(SELECT id FROM users WHERE nickname=?),\n" + "(SELECT id FROM users WHERE nickname=?),\n" + "?)");
    }
    public  static String getNickname(String login,String password){
        String nick = null;
      try {
          getNickname.setString(1,login);
          getNickname.setString(2,password);
          ResultSet resultSet = getNickname.executeQuery();
          if(resultSet.next()){
             nick = resultSet.getString(1);
          }
          resultSet.close();
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return nick;
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean changeNick(String oldNick,String newNick){
        try {
            changeNick.setString(1,newNick);
            changeNick.setString(2,oldNick);
            changeNick.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }

    }
    public static boolean saveMessage(String sender,String receiver,String text){
        try {
            saveMessage.setString(1,sender);
            saveMessage.setString(2,receiver);
            saveMessage.setString(3,text);
            saveMessage.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }



}

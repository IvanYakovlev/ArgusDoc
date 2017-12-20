package dbConnection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconnection extends SQLException{

    private static final String URL = "jdbc:sqlserver://95.188.110.171:1433;database=ArgusDoc";

    private static final String USER = "sql_admin";

    private static final String PASSWORD = "Qaz123wsx456";

    private  static Connection connection;

    public static void connect(){

        try {
            Driver driver = new com.microsoft.sqlserver.jdbc.SQLServerDriver();
            DriverManager.registerDriver(driver);

            connection=DriverManager.getConnection(URL,USER,PASSWORD);
            if(!connection.isClosed()){
                System.out.println("Соединение с БД установлено!");

            }

              } catch (Exception e) {
            System.out.println("Ошибка соединения!");
        }
    }
    public static void disconect() throws SQLException {
        connection.close();
        System.out.println("Соединение с БД закрыто!");
    }
}

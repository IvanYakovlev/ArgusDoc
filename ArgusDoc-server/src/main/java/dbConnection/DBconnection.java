package dbConnection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconnection{

    private String URL = "jdbc:sqlserver://95.188.110.171:1433;database=ArgusDoc";

    private String USER = "sql_admin";

    private String PASSWORD = "Qaz123wsx456";

    private Connection connection;

    public DBconnection() {

    }

    public Connection connect(){
        if (this.connection==null) {
            try {
                Driver driver = new com.microsoft.sqlserver.jdbc.SQLServerDriver();
                DriverManager.registerDriver(driver);
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                if (!connection.isClosed()) {
                    System.out.println("Соединение с БД установлено!");
                }
/*                connection.close();
                System.out.println("Соединение с БД закрыто!");*/

            } catch (Exception e) {
                System.out.println("Ошибка соединения!");
            }
        }
        return this.connection;
    }
    public void close(){
        try {
            connection.close();
            connection=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Соединение с БД закрыто!");
    }
}

package dbConnection;

import com.sun.security.ntlm.Server;
import controller.ServerController;
import dialog.ADInfo;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBconnection{

    public static String URL ;

    public static String USER ;

    public static String PASSWORD ;

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

            } catch (SQLException e) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Соединение с БД не установлено");
                System.exit(0);

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

package controller;

import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXPasswordField;
import dbConnection.DBconnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Employee;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationController {
    @FXML
    private JFXPasswordField txtPasswordEnter;
    DBconnection dBconnection;

        public JFXPasswordField getTxtPasswordEnter() {
        return txtPasswordEnter;
    }

    private Employee userAuth=new Employee();




    private double xOffset;
    private double yOffset;


    public Employee getUserAuth() {
        return userAuth;
    }

    public void loginButton(ActionEvent actionEvent) {



            try {
                this.dBconnection = new DBconnection();

                ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id AND Employee_password = '"+txtPasswordEnter.getText()+"'");
                while (resultSet.next()) {
                    userAuth.setEmployeeId(resultSet.getInt("Employee_id"));
                    userAuth.setEmployeeName(resultSet.getString("Employee_name"));
                    userAuth.setEmployeeLogin(resultSet.getString("Employee_login"));
                    userAuth.setEmployeePassword(resultSet.getString("Employee_password"));
                    userAuth.setDepartmentName(resultSet.getString("Department_name"));
                    userAuth.setAccessName(resultSet.getString("Access_name"));
                    userAuth.setDepartmentId(resultSet.getInt("Department_id"));
                    userAuth.setAccessId(resultSet.getInt("Access_id"));
                    userAuth.setEmployeeOnline(resultSet.getByte("Employee_online"));
                    AuthorizedUser.setUser(userAuth);

                }

            } catch (SQLException e) {

            }
            if (userAuth.getEmployeeName()==null){
                dialog.ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Пользователь не найден");
            } else {
                try {

                    ((Node) actionEvent.getSource()).getScene().getWindow().hide();

                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();

                    Parent root = loader.load(getClass().getResource("/viewFXML/Main_window2.fxml"));
                    stage.setTitle("Аргус");
                    stage.setMinHeight(715);
                    stage.setMinWidth(1000);
                    stage.getIcons().add(new Image("images/icon.jpg"));
                    stage.setScene(new Scene(root));

                    stage.initStyle(StageStyle.TRANSPARENT);

                    root.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            xOffset = event.getSceneX();
                            yOffset = event.getSceneY();
                        }
                    });
                    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            stage.setX(event.getScreenX() - xOffset);
                            stage.setY(event.getScreenY() - yOffset);
                        }
                    });


                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }


}

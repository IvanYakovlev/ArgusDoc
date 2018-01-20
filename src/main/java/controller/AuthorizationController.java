package controller;

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
import javafx.scene.control.TextField;
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
    Employee user;
    private double xOffset;
    private double yOffset;
    public void loginButton(ActionEvent actionEvent) {



            try {
                this.dBconnection = new DBconnection();

                ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id AND Employee_password = '"+txtPasswordEnter.getText()+"'");
                this.user = new Employee();
                while (resultSet.next()) {
                    this.user.setEmployeeId(resultSet.getInt("Employee_id"));
                    this.user.setEmployeeName(resultSet.getString("Employee_name"));
                    this.user.setEmployeeLogin(resultSet.getString("Employee_login"));
                    this.user.setEmployeePassword(resultSet.getString("Employee_password"));
                    this.user.setDepartmentName(resultSet.getString("Department_name"));
                    this.user.setAccessName(resultSet.getString("Access_name"));
                    this.user.setDepartmentId(resultSet.getInt("Department_id"));
                    this.user.setAccessId(resultSet.getInt("Access_id"));
                    this.user.setEmployeeOnline(resultSet.getByte("Employee_online"));
                }

            } catch (SQLException e) {

            }
            if (user.getEmployeeLogin()==null){
                dialog.ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Пользователь не найден");
            } else {
                try {
                    ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/viewFXML/Main_window.fxml"));
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

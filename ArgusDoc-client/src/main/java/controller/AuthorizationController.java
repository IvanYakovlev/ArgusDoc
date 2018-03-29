package controller;

import com.jfoenix.controls.JFXPasswordField;

import com.jfoenix.controls.JFXTextField;
import dialog.ADInfo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import entity.Employee;
import service.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Properties;

public class AuthorizationController {


    @FXML
    private JFXPasswordField txtPasswordEnter;
    @FXML
    private JFXTextField txtIpAddress;
    @FXML
    private JFXTextField txtPort;


    private EmployeeService employeeService;

    private double xOffset;
    private double yOffset;
    private Properties properties;
    private FileInputStream in;

    public void initialize() throws IOException {
        properties = new Properties();
        in = new FileInputStream("../ArgusDoc/ArgusDoc-client/src/main/resources/ArgusDocClient.properties");
        properties.load(in);

        txtIpAddress.setText(properties.getProperty("ipAddress"));
        txtPort.setText(properties.getProperty("port"));

        txtPort.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPort.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void loginButton(ActionEvent actionEvent) throws IOException {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        enter();

    }



    public void enter() throws IOException {


        ServiceRegistry.ipAddress=txtIpAddress.getText();

        ServiceRegistry.port= Integer.parseInt(txtPort.getText());

        ServiceRegistry.init();

        employeeService = ServiceRegistry.employeeService;

        Employee authorizedUser = null;

        try {

            authorizedUser = employeeService.getEmployeeByPassword(txtPasswordEnter.getText());

            if (authorizedUser.getEmployeeName() == null) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь не найден!");
                Platform.exit();
            } else {


                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(getClass().getResource("/viewFXML/Main_window.fxml"));


                try {

                    fxmlLoader.load();
                    Stage stage = new Stage();
                    Parent root = fxmlLoader.getRoot();
                    stage.setScene(new Scene(root));

                    stage.setTitle("Аргус");
                    stage.setMinHeight(715);
                    stage.setMinWidth(1000);
                    stage.getIcons().add(new Image("images/1.jpg"));
                    MainController mainController = fxmlLoader.getController();
                    mainController.initialize(authorizedUser,stage);


                    stage.show();
// если успешно подключились сохраняем IP и Порт
                    properties.setProperty("ipAddress", txtIpAddress.getText());
                    properties.setProperty("port", txtPort.getText());
                    FileOutputStream outputStream = new FileOutputStream("../ArgusDoc/ArgusDoc-client/src/main/resources/ArgusDocClient.properties");
                    properties.store(outputStream, "");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();

        }catch (NoSuchObjectException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            //e.printStackTrace();
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Не удалось соединиться с сервером!");
            Platform.exit();
        }




    }

    public void enterKeyPress(KeyEvent keyEvent) {
        txtPasswordEnter.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        enter();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ((Node) keyEvent.getSource()).getScene().getWindow().hide();
                }
            }
        });


    }


    public void cancelButton(ActionEvent actionEvent) {
        Platform.exit();
    }
}

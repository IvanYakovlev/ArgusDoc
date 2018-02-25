package controller;

import authorizedUser.AuthorizedUser;
import com.jfoenix.controls.JFXPasswordField;

import dialog.ADInfo;
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
import main.Main;
import service.*;


import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorizationController {


    @FXML
    private JFXPasswordField txtPasswordEnter;

    public JFXPasswordField getTxtPasswordEnter() {
        return txtPasswordEnter;
    }

    private Employee userAuth;



    public Main main;
/*    private DepartmentService departmentService;
    private EmployeeService employeeService;
    private AccessService accessService;
    private DocumentService documentService;
    private LetterService letterService;
    private TaskService taskService;
    private EventService eventService;*/
    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;
    private double xOffset;
    private double yOffset;


    public Employee getUserAuth() {
        return userAuth;
    }

    public void loginButton(ActionEvent actionEvent) throws RemoteException {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        enter();

    }



    public void enter() throws RemoteException {
        try {
            userAuth = employeeService.getEmployeeByPassword(txtPasswordEnter.getText());


            AuthorizedUser.setUser(userAuth);

//        System.out.println(employeeService.listEmployees());
            if (userAuth.getEmployeeName() == null) {
                dialog.ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Пользователь не найден");
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
                    stage.getIcons().add(new Image("images/icon.jpg"));


                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            /*try {



                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../viewFXML/Main_window.fxml"));
                System.out.println(main);

                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.initService();


                stage.setTitle("Аргус");
                stage.setMinHeight(715);
                stage.setMinWidth(1000);
                stage.getIcons().add(new Image("images/icon.jpg"));

                stage.setScene(new Scene(root));


                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            }
        }   catch (NullPointerException e){
        ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Сервер недоступен!");
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
                    }
                    ((Node) keyEvent.getSource()).getScene().getWindow().hide();
                }
            }
        });


    }


    public DepartmentService getDepartmentService() {
        return departmentService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public AccessService getAccessService() {
        return accessService;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public LetterService getLetterService() {
        return letterService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public EventService getEventService() {
        return eventService;
    }
}

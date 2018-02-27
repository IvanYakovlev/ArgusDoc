package controller;

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
import service.*;


import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class AuthorizationController {


    @FXML
    private JFXPasswordField txtPasswordEnter;





    private DepartmentService departmentService = ServiceRegistry.departmentService;
    private EmployeeService employeeService = ServiceRegistry.employeeService;
    private AccessService accessService = ServiceRegistry.accessService;
    private DocumentService documentService = ServiceRegistry.documentService;
    private LetterService letterService = ServiceRegistry.letterService;
    private TaskService taskService = ServiceRegistry.taskService;
    private EventService eventService = ServiceRegistry.eventService;
    private double xOffset;
    private double yOffset;



    public void loginButton(ActionEvent actionEvent) throws RemoteException {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        enter();

    }



    public void enter() throws RemoteException {

        Employee authorizedUser = null;
        try {
            authorizedUser = employeeService.getEmployeeByPassword(txtPasswordEnter.getText());

            if (authorizedUser.getEmployeeName() == null) {
                dialog.ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Пользователь не найден");
            } else {


                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(getClass().getResource("/viewFXML/Main_window.fxml"));


                try {

                    fxmlLoader.load();
                    Stage stage = new Stage();
                    Parent root = fxmlLoader.getRoot();
                    stage.setScene(new Scene(root));
                    MainController mainController = fxmlLoader.getController();
                    mainController.initialize(authorizedUser);
                    stage.setTitle("Аргус");
                    stage.setMinHeight(715);
                    stage.setMinWidth(1000);
                    stage.getIcons().add(new Image("images/icon2.jpg"));


                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (SQLException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь не найден!");
        }catch (NoSuchObjectException e){
            System.out.println("ШО ЗА НАХ");
        }catch (NullPointerException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Сервер незапущен!");
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

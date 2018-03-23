package controller;

import com.jfoenix.controls.JFXButton;
import dbConnection.DBconnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import service.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;


public class ServerController {
    DBconnection dBconnection = new DBconnection();

    Registry registry;
    AccessServiceImpl accessServiceImpl;
    AccessService accessService;

    DepartmentServiceImpl departmentServiceImpl;
    DepartmentService departmentService;

    DocumentServiceImpl documentServiceImpl;
    DocumentService documentService;

    EmployeeServiceImpl employeeServiceImpl;
    EmployeeService employeeService;

    EventServiceImpl eventServiceImpl;
    EventService eventService;

    LetterServiceImpl letterServiceImpl;
    LetterService letterService;

    TaskServiceImpl taskServiceImpl;
    TaskService taskService;

    String serverCondition = "stopped";

    @FXML
    private JFXButton stopServerButton;
    @FXML
    private JFXButton startServerButton;
    public void initialize() throws RemoteException {
        stopServerButton.setDisable(true);
        startServer();

    }
    public void startServerButton(ActionEvent actionEvent) throws RemoteException {
        startServer();
    }


    public void stopServerButton(ActionEvent actionEvent) throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вы действительно хотите остановить сервер?");

        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {

        } else if (option.get() == ButtonType.OK) {

            startServerButton.setDisable(false);
            stopServerButton.setDisable(true);
            stopServer();

        } else if (option.get() == ButtonType.CANCEL) {

        } else {

        }
    }

    public void closeServer(MouseEvent mouseEvent) {
        if (serverCondition.equals("running")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("При выходе из программы сервер будет остановлен. Вы действительно хотите выйти?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                if (!serverCondition.equals("stopped")) {
                    stopServer();
                }

                ((Node) mouseEvent.getSource()).getScene().getWindow().hide();


            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }
        } else {
            ((Node) mouseEvent.getSource()).getScene().getWindow().hide();
        }
    }
    private void startServer() throws RemoteException {
        dBconnection.connect();

        registry = LocateRegistry.createRegistry(8966);
        accessServiceImpl = new AccessServiceImpl();
        accessService = (AccessService) UnicastRemoteObject.exportObject(accessServiceImpl,0);
        registry.rebind("accessService", accessService);

        departmentServiceImpl = new DepartmentServiceImpl();
        departmentService = (DepartmentService) UnicastRemoteObject.exportObject(departmentServiceImpl, 0);
        registry.rebind("departmentService", departmentService);

        documentServiceImpl = new DocumentServiceImpl();
        documentService = (DocumentService) UnicastRemoteObject.exportObject(documentServiceImpl, 0);
        registry.rebind("documentService",documentService);

        employeeServiceImpl = new EmployeeServiceImpl();
        employeeService = (EmployeeService) UnicastRemoteObject.exportObject(employeeServiceImpl, 0);
        registry.rebind("employeeService",employeeService);

        eventServiceImpl = new EventServiceImpl();
        eventService = (EventService) UnicastRemoteObject.exportObject(eventServiceImpl,0);
        registry.rebind("eventService", eventService);

        letterServiceImpl = new LetterServiceImpl();
        letterService = (LetterService) UnicastRemoteObject.exportObject(letterServiceImpl,0);
        registry.rebind("letterService", letterService);

        taskServiceImpl = new TaskServiceImpl();
        taskService = (TaskService) UnicastRemoteObject.exportObject(taskServiceImpl, 0);
        registry.rebind("taskService", taskService);

        System.out.println("Сервер запущен..");
        serverCondition = "running";
        startServerButton.setDisable(true);
        stopServerButton.setDisable(false);
    }

    public void stopServer(){

        dBconnection.connect();

        try {

            UnicastRemoteObject.unexportObject(accessServiceImpl, true);
            registry.unbind("accessService");


            UnicastRemoteObject.unexportObject(departmentServiceImpl, true);
            registry.unbind("departmentService");


            UnicastRemoteObject.unexportObject(documentServiceImpl, true);
            registry.unbind("documentService");


            UnicastRemoteObject.unexportObject(employeeServiceImpl, true);
            registry.unbind("employeeService");


            UnicastRemoteObject.unexportObject(eventServiceImpl, true);
            registry.unbind("eventService");


            UnicastRemoteObject.unexportObject(letterServiceImpl, true);
            registry.unbind("letterService");


            UnicastRemoteObject.unexportObject(taskServiceImpl, true);
            registry.unbind("taskService");

            UnicastRemoteObject.unexportObject(registry, true);

            registry=null;
            dBconnection.close();

        }   catch (NotBoundException e) {
            e.printStackTrace();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Сервер остановлен.");
        serverCondition="stopped";
    }
}

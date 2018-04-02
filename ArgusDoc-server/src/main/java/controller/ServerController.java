package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dbConnection.DBconnection;
import dialog.ADInfo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.print.DocFlavor;
import java.awt.*;
import java.io.*;
import java.net.BindException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ServerController {
    DBconnection dBconnection;

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
    @FXML
    private JFXTextField txtDBURL;

    @FXML
    private JFXTextField txtDBUser;

    @FXML
    private JFXTextField txtDBPassword;

    @FXML
    private JFXTextField txtDBServerPort;



    public Stage stage;

    public static int serverPort;

    private Properties properties;
    private FileInputStream in;
    private OutputStream out;
    public static java.awt.SystemTray tray;
    public static  java.awt.TrayIcon trayIcon;

    public void initialize() throws IOException {

        try {
            //Инициализируем toolkit
            java.awt.Toolkit.getDefaultToolkit();

            //Проверка на поддержку в трее
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            //создаем трей иконку
            tray = java.awt.SystemTray.getSystemTray();
            trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("ArgusDoc-server/src/main/resources/images/trayIcon.jpg"));

            //двойное нажатие мыши - показываем stage
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            //выбор меню по умолчанию - показываем stage
            java.awt.MenuItem openItem = new java.awt.MenuItem("Восстановить");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            // Устанавливаем шрифт для пункта меню по умолчанию
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            //меню выход из приложения, удаляем из трея и закрываем программу
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Выход");
            exitItem.addActionListener(event -> {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (serverCondition.equals("running")) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.getIcons().add(new Image("/images/1.jpg"));
                            alert.setTitle("Выход");
                            alert.setHeaderText("При выходе из программы сервер будет остановлен. Вы действительно хотите выйти?");

                            // option != null.
                            Optional<ButtonType> option = alert.showAndWait();

                            if (option.get() == null) {

                            } else if (option.get() == ButtonType.OK) {
                                if (!serverCondition.equals("stopped")) {
                                    stopServer();
                                }

                                Platform.exit();
                                tray.remove(trayIcon);


                            } else if (option.get() == ButtonType.CANCEL) {

                            } else {

                            }
                        } else {
                            Platform.exit();
                            tray.remove(trayIcon);
                        }
                    }
                });

            });

            // устанавливаем  popup меню для приложения
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);


            // добавляем иконку в трей
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }

        txtDBServerPort.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtDBServerPort.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        properties = new Properties();
        in = new FileInputStream("../ArgusDoc/ArgusDoc-server/src/main/resources/ArgusDocServer.properties");
        properties.load(in);

        txtDBURL.setText(properties.getProperty("URL"));
        txtDBUser.setText(properties.getProperty("User"));
        txtDBPassword.setText(properties.getProperty("Password"));
        txtDBServerPort.setText(properties.getProperty("Port"));

        stopServerButton.setDisable(true);
        //startServer();

    }
    public void startServerButton(ActionEvent actionEvent) throws RemoteException {
        startServer();
    }


    public void stopServerButton(ActionEvent actionEvent) throws RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/images/1.jpg"));
        alert.setTitle("Остановка сервера");
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


    private void startServer() throws RemoteException {

        DBconnection.URL=txtDBURL.getText();
        DBconnection.USER=txtDBUser.getText();
        DBconnection.PASSWORD=txtDBPassword.getText();
        try {
            serverPort= Integer.parseInt(txtDBServerPort.getText());
        }catch (NumberFormatException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Некорректный порт!");
            System.exit(0);

        }



        dBconnection=new DBconnection();
        dBconnection.connect();
        try {
            registry = LocateRegistry.createRegistry(serverPort);
        }catch (IllegalArgumentException e){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Некорректный порт!");
            System.exit(0);
        }catch (ExportException e1){
            ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Порт занят!");
            System.exit(0);
        }

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


    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
        System.out.println("work");
    }
    public void initStage(Stage stage){
        this.stage = stage;
    }

    public void minimizeServer(MouseEvent mouseEvent) {
        stage.hide();
    }

    public void writeSettings(ActionEvent actionEvent) throws IOException {
        // если успешно подключились сохраняем IP и Порт
        properties.setProperty("URL", txtDBURL.getText());
        properties.setProperty("User", txtDBUser.getText());
        properties.setProperty("Password", txtDBPassword.getText());
        properties.setProperty("Port", txtDBServerPort.getText());

        FileOutputStream outputStream = new FileOutputStream("../ArgusDoc/ArgusDoc-server/src/main/resources/ArgusDocServer.properties");
        properties.store(outputStream, "");

    }
}

import dbConnection.DBconnection;
import javafx.application.Application;
import javafx.stage.Stage;
import service.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main extends Application{
    DBconnection dBconnection = new DBconnection();


    @Override
    public void start(Stage primaryStage) throws Exception {
        dBconnection.connect();

        Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

        AccessServiceImpl accessServiceImpl = new AccessServiceImpl();
        AccessService accessService = (AccessService) UnicastRemoteObject.exportObject(accessServiceImpl,0);
        registry.rebind("accessService", accessService);

        DepartmentServiceImpl departmentServiceImpl = new DepartmentServiceImpl();
        DepartmentService departmentService = (DepartmentService) UnicastRemoteObject.exportObject(departmentServiceImpl, 0);
        registry.rebind("departmentService", departmentService);

        DocumentServiceImpl documentServiceImpl = new DocumentServiceImpl();
        DocumentService documentService = (DocumentService) UnicastRemoteObject.exportObject(documentServiceImpl, 0);
        registry.rebind("documentService",documentService);

        EmployeeServiceImpl employeeServiceImpl = new EmployeeServiceImpl();
        EmployeeService employeeService = (EmployeeService) UnicastRemoteObject.exportObject(employeeServiceImpl, 0);
        registry.rebind("employeeService",employeeService);

        EventServiceImpl eventServiceImpl = new EventServiceImpl();
        EventService eventService = (EventService) UnicastRemoteObject.exportObject(eventServiceImpl,0);
        registry.rebind("eventService", eventService);

        LetterServiceImpl letterServiceImpl = new LetterServiceImpl();
        LetterService letterService = (LetterService) UnicastRemoteObject.exportObject(letterServiceImpl,0);
        registry.rebind("letterService", letterService);

        TaskServiceImpl taskServiceImpl = new TaskServiceImpl();
        TaskService taskService = (TaskService) UnicastRemoteObject.exportObject(taskServiceImpl, 0);
        registry.rebind("taskService", taskService);

        System.out.println("Сервер запущен..");
    }
    public static void main(String[] args) throws RemoteException {
        launch(args);
    }
}

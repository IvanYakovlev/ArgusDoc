package service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServiceRegistry {

    public static Registry registry;
    public static AccessService accessService;
    public static DepartmentService departmentService;
    public static DocumentService documentService;
    public static EmployeeService employeeService;
    public static EventService eventService;
    public static LetterService letterService;
    public static TaskService taskService;
    public static String ipAddress;
    public static int port;

//Подключение к серверу инициализация registry и сервисов
    public static void init() {
        try {
            registry = LocateRegistry.getRegistry(ipAddress, port);
            accessService = (AccessService) registry.lookup("accessService");
            departmentService = (DepartmentService) registry.lookup("departmentService");
            documentService = (DocumentService) registry.lookup("documentService");
            employeeService = (EmployeeService) registry.lookup("employeeService");
            eventService = (EventService) registry.lookup("eventService");
            letterService = (LetterService) registry.lookup("letterService");
            taskService = (TaskService) registry.lookup("taskService");

        } catch (RemoteException e) {
            System.out.println("сервер не запущен");
        } catch (NotBoundException e) {
            System.out.println("сервер не запущен");
        }
    }

}

package service;

import javafx.collections.ObservableList;
import entity.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeService extends Remote {

    public void addEmployee(Employee employee) throws RemoteException;
    public void updateEmployee(Employee employee) throws RemoteException;
    public void removeEmployee(int id) throws RemoteException;
    public List<Employee> listEmployees() throws RemoteException;
    public List<String> listEmployeesName() throws RemoteException;
    public List<String> listEmployeesNameJurist() throws RemoteException;
    public List<String> listEmployeesNameTechnical() throws RemoteException;
    public List<String> listEmployeesNameOrip() throws RemoteException;
    public List<String> listEmployeesNameBookkeeping() throws RemoteException;

    public int getIdEmployeeByName(String value) throws RemoteException;
    public Employee getEmployeeByPassword(String password) throws RemoteException;
}

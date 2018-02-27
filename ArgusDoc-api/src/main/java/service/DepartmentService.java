package service;

import javafx.collections.ObservableList;
import entity.Department;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DepartmentService extends Remote {
    public void addDepartment(Department department) throws RemoteException, SQLException;
    public void updateDepartment(Department department) throws RemoteException, SQLException;
    public void removeDepartment(int id) throws RemoteException, SQLException;
    public List<Department> listDepartments() throws RemoteException;
    public List<String> listDepartmentName() throws RemoteException;
    public int getIdDepartmentByName(String value) throws RemoteException;

}

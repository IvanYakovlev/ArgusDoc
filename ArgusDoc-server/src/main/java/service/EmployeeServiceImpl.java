package service;


import dbConnection.DBconnection;

import entity.Employee;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeServiceImpl implements EmployeeService {

    DBconnection dBconnection;
    Map<Integer, String> mapEmployee = new HashMap<Integer, String>();
    List<Employee> listEmployee = new ArrayList<Employee>();

    public void addEmployee(Employee employee) throws RemoteException, SQLException {
        this.dBconnection = new DBconnection();

            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Employees(Employee_name,Employee_password,Department_id,Access_id) VALUES (?,?,?,?)");
            preparedStatement.setString(1,employee.getEmployeeName());

            preparedStatement.setString(2,employee.getEmployeePassword());
            preparedStatement.setInt(3,employee.getDepartmentId());
            preparedStatement.setInt(4,employee.getAccessId());
            preparedStatement.execute();

    }

    @Override
    public void updateEmployee(Employee employee) throws RemoteException, SQLException {
        this.dBconnection = new DBconnection();

            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("UPDATE Employees SET Employee_name=?,Employee_password=?,Department_id=?,Access_id=? WHERE Employee_id=?");
            preparedStatement.setString(1,employee.getEmployeeName());

            preparedStatement.setString(2,employee.getEmployeePassword());
            preparedStatement.setInt(3,employee.getDepartmentId());
            preparedStatement.setInt(4,employee.getAccessId());
            preparedStatement.setInt(5,employee.getEmployeeId());
            preparedStatement.execute();

    }

    public void removeEmployee(int id) throws RemoteException, SQLException {
        this.dBconnection = new DBconnection();

            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("DELETE FROM Employees WHERE Employee_id=?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();



    }

    public List<Employee> listEmployees() throws RemoteException{
        this.dBconnection = new DBconnection();
        List<Employee> listData = new ArrayList<Employee>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id");
            while (resultSet.next()){
                Employee employee= new Employee();
                    employee.setEmployeeId(resultSet.getInt("Employee_id"));
                    employee.setEmployeeName(resultSet.getString("Employee_name"));
                    employee.setEmployeePassword(resultSet.getString("Employee_password"));
                    employee.setDepartmentName(resultSet.getString("Department_name"));
                    employee.setAccessName(resultSet.getString("Access_name"));
                    mapEmployee.put(employee.getEmployeeId(),employee.getEmployeeName());
                    if (!listEmployee.contains(employee)) {
                        listEmployee.add(employee);
                    }
                listData.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public List<String> listEmployeesName() throws RemoteException{
        List<String> listData = new ArrayList<String>();
        for(Map.Entry<Integer, String> e : mapEmployee.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public List<String> listEmployeesNameJurist() throws RemoteException {
        List<String> listData = new ArrayList<String>();
        for(int i = 0; i< listEmployee.size(); i++) {
            if (listEmployee.get(i).getDepartmentName().equals("Юридический отдел")&&!listData.contains(listEmployee.get(i).getEmployeeName()))
            listData.add(listEmployee.get(i).getEmployeeName());
        }
        return listData;
    }

    @Override
    public List<String> listEmployeesNameTechnical() throws RemoteException {
        List<String> listData = new ArrayList<String>();
        for(int i = 0; i< listEmployee.size(); i++) {
            if (listEmployee.get(i).getDepartmentName().equals("Технический отдел")&&!listData.contains(listEmployee.get(i).getEmployeeName()))
                listData.add(listEmployee.get(i).getEmployeeName());
        }
        return listData;
    }

    @Override
    public List<String> listEmployeesNameOrip() throws RemoteException {
        List<String> listData = new ArrayList<String>();
        for(int i = 0; i< listEmployee.size(); i++) {
            if (listEmployee.get(i).getDepartmentName().equals("ОРиП")&&!listData.contains(listEmployee.get(i).getEmployeeName()))
                listData.add(listEmployee.get(i).getEmployeeName());
        }
        return listData;
    }

    @Override
    public List<String> listEmployeesNameBookkeeping() throws RemoteException {
        List<String> listData = new ArrayList<String>();
        for(int i = 0; i< listEmployee.size(); i++) {
            if (listEmployee.get(i).getDepartmentName().equals("Бухгалтерия")&&!listData.contains(listEmployee.get(i).getEmployeeName()))
                listData.add(listEmployee.get(i).getEmployeeName());
        }
        return listData;
    }

    @Override
    public int getIdEmployeeByName(String value) throws RemoteException{
        int key=0;
        for(Map.Entry<Integer, String> e : mapEmployee.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }

    @Override
    public Employee getEmployeeByPassword(String password) throws RemoteException, SQLException {
        Employee employee= new Employee();

            this.dBconnection = new DBconnection();

            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id AND Employee_password = '"+password+"'");
            while (resultSet.next()) {
                employee.setEmployeeId(resultSet.getInt("Employee_id"));
                employee.setEmployeeName(resultSet.getString("Employee_name"));
                employee.setEmployeePassword(resultSet.getString("Employee_password"));
                employee.setDepartmentName(resultSet.getString("Department_name"));
                employee.setAccessName(resultSet.getString("Access_name"));
                employee.setDepartmentId(resultSet.getInt("Department_id"));
                employee.setAccessId(resultSet.getInt("Access_id"));
                employee.setEmployeeOnline(resultSet.getByte("Employee_online"));


            }


        return employee;
    }
}

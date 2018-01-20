package dao;


import dialog.ADInfo;
import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao{

    DBconnection dBconnection;
    Map<Integer, String> mapEmployee = new HashMap<Integer, String>();

    public void addEmployee(Employee employee) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Employees(Employee_name,Employee_login,Employee_password,Department_id,Access_id) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1,employee.getEmployeeName());
            preparedStatement.setString(2,employee.getEmployeeLogin());
            preparedStatement.setString(3,employee.getEmployeePassword());
            preparedStatement.setInt(4,employee.getDepartmentId());
            preparedStatement.setInt(5,employee.getAccessId());
            preparedStatement.execute();
        } catch (SQLException e) {
            //e.printStackTrace();
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь уже существует!");
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("UPDATE Employees SET Employee_name=?,Employee_login=?,Employee_password=?,Department_id=?,Access_id=? WHERE Employee_id=?");
            preparedStatement.setString(1,employee.getEmployeeName());
            preparedStatement.setString(2,employee.getEmployeeLogin());
            preparedStatement.setString(3,employee.getEmployeePassword());
            preparedStatement.setInt(4,employee.getDepartmentId());
            preparedStatement.setInt(5,employee.getAccessId());
            preparedStatement.setInt(6,employee.getEmployeeId());
            preparedStatement.execute();
        } catch (SQLException e) {
           e.printStackTrace();
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Данный пользователь уже существует!");
        }
    }

    public void removeEmployee(int id) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("DELETE FROM Employees WHERE Employee_id=?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public ObservableList<Employee> listEmployees() {
        this.dBconnection = new DBconnection();
        ObservableList<Employee> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id");
            while (resultSet.next()){
                Employee employee= new Employee();
                    employee.setEmployeeId(resultSet.getInt("Employee_id"));
                    employee.setEmployeeName(resultSet.getString("Employee_name"));
                    employee.setEmployeeLogin(resultSet.getString("Employee_login"));
                    employee.setEmployeePassword(resultSet.getString("Employee_password"));
                    employee.setDepartmentName(resultSet.getString("Department_name"));
                    employee.setAccessName(resultSet.getString("Access_name"));
                    mapEmployee.put(employee.getEmployeeId(),employee.getEmployeeName());
                listData.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public int getIdEmployeeByName(String value) {
        int key=0;
        for(Map.Entry<Integer, String> e : mapEmployee.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }
}

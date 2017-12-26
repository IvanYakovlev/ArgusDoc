package dao;


import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao{
    DBconnection dBconnection;
    public void addEmployee(Employee employee) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Employees(Employee_name,Employee_login,Employee_password,Department_id,Access_id) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1,employee.getEmployeeName());
            preparedStatement.setString(2,employee.getEmployeeLogin());
            preparedStatement.setString(3,employee.getEmployeePassword());
            preparedStatement.setInt(4,employee.getDepartmentID());
            preparedStatement.setInt(5,employee.getAccessId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
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
            preparedStatement.setInt(4,employee.getDepartmentID());
            preparedStatement.setInt(5,employee.getAccessId());
            preparedStatement.setInt(6,employee.getEmployeeId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
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
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees");
            while (resultSet.next()){
                Employee employee= new Employee();
                    employee.setEmployeeId(resultSet.getInt("Employee_id"));
                    employee.setEmployeeName(resultSet.getString("Employee_name"));
                    employee.setEmployeeLogin(resultSet.getString("Employee_login"));
                    employee.setEmployeePassword(resultSet.getString("Employee_password"));
                    employee.setDepartmentID(resultSet.getInt("Department_id"));
                    employee.setAccessId(resultSet.getInt("Access_id"));
                listData.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

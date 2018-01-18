package user;

import dbConnection.DBconnection;
import model.Employee;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuth {
    Employee user;
    DBconnection dBconnection;

    public UserAuth(String password) {
        this.dBconnection = new DBconnection();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM Employees, Departments,Access WHERE Employees.Department_id=Departments.Department_id AND Access.Access_id=Employees.Access_id AND Employee_password = '"+password+"'");
            this.user = new Employee();
            while (resultSet.next()) {
                this.user.setEmployeeId(resultSet.getInt("Employee_id"));
                this.user.setEmployeeName(resultSet.getString("Employee_name"));
                this.user.setEmployeeLogin(resultSet.getString("Employee_login"));
                this.user.setEmployeePassword(resultSet.getString("Employee_password"));
                this.user.setDepartmentName(resultSet.getString("Department_name"));
                this.user.setAccessName(resultSet.getString("Access_name"));
                this.user.setDepartmentId(resultSet.getInt("Department_id"));
                this.user.setAccessId(resultSet.getInt("Access_id"));
                this.user.setEmployeeOnline(resultSet.getByte("Employee_online"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserAuth{" +
                "Пользователь - " + user.getEmployeeName() +
                ", Отдел - " + user.getDepartmentName() +
                ", Доступ - " + user.getAccessName() +
                '}';
    }
}

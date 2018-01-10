package model;

import javax.persistence.*;
@Entity
@Table(name = "EMPLOYEES")
public class Employee {
    @Id
    @Column(name = "Employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int employeeId;

    @Column(name = "Employee_name")
    private String employeeName;

    @Column(name = "Employee_login")
    private String employeeLogin;

    @Column(name = "Employee_password")
    private String employeePassword;

    @Column(name = "Department_id")
    private String departmentID;

    @Column(name = "Access_id")
    private String accessId;


    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeLogin() {
        return employeeLogin;
    }

    public void setEmployeeLogin(String employeeLogin) {
        this.employeeLogin = employeeLogin;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String  getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeLogin='" + employeeLogin + '\'' +
                ", employeePassword='" + employeePassword + '\'' +
                ", departmentID=" + departmentID +
                ", accessId=" + accessId +
                '}';
    }
}

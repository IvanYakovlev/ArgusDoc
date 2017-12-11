package model;

import javax.persistence.*;
@Entity
@Table(name = "EMPLOYEES")
public class Employees {
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
    private int departmentID;

    @Column(name = "Access_id")
    private int accessId;


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

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeLogin='" + employeeLogin + '\'' +
                ", employeePassword='" + employeePassword + '\'' +
                ", departmentID=" + departmentID +
                ", accessId=" + accessId +
                '}';
    }
}

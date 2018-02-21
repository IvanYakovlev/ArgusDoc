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


    private String departmentName;


    private String accessName;

    @Column(name = "Department_id")
    private int departmentId;

    @Column(name = "Access_id")
    private int accessId;

    @Column(name = "Employee_online")
    private int employeeOnline;

    public int getEmployeeOnline() {
        return employeeOnline;
    }

    public void setEmployeeOnline(int employeeOnline) {
        this.employeeOnline = employeeOnline;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeLogin='" + employeeLogin + '\'' +
                ", employeePassword='" + employeePassword + '\'' +
                ", departmentName=" + departmentName +
                ", accessName=" + accessName +
                '}';
    }
}

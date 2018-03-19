package entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "DEPARTMENTS")
public class Department extends RecursiveTreeObject<Department> implements Serializable{

    /*Entity класс соответсвующий таблице DEPARTMENTS*/

    @Id
    @Column(name = "Department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;

    @Column(name = "Department_name")
    private String departmentName;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }

}

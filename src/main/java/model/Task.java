package model;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.io.File;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "TASKS")
public class Task extends RecursiveTreeObject<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Task_id")
    private int taskId;

    @Column(name = "Task_name")
    private String taskName;

    @Column(name = "Task_text")
    private String taskText;

    @Column(name = "Task_attachment")
    private String taskAttachment;

    @Column(name = "Task_from_employee")
    private String taskFromEmployee;

    @Column(name = "Employee_id")
    private int employeeId;

    private String employeeName;

    @Column(name = "Task_term")
    private java.sql.Date taskTerm;

    @Column(name = "Task_time")
    private java.sql.Time taskTime;

    @Column(name = "Status_task_id")
    private int statusTaskId;
    private String statusTaskName;

    private File taskAttachmentFile;

    private Boolean taskIsLetter;

    public Boolean getTaskIsLetter() {
        return taskIsLetter;
    }

    public void setTaskIsLetter(Boolean taskIsLetter) {
        this.taskIsLetter = taskIsLetter;
    }

    public File getTaskAttachmentFile() {
        return taskAttachmentFile;
    }

    public void setTaskAttachmentFile(File taskAttachmentFile) {
        this.taskAttachmentFile = taskAttachmentFile;
    }

    public Time getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Time taskTime) {
        this.taskTime = taskTime;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getStatusTaskId() {
        return statusTaskId;
    }

    public void setStatusTaskId(int statusTaskId) {
        this.statusTaskId = statusTaskId;
    }

    public String getStatusTaskName() {
        return statusTaskName;
    }

    public void setStatusTaskName(String statusTaskName) {
        this.statusTaskName = statusTaskName;
    }

    public int getTaskId() {
        return taskId;
    }


    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskAttachment() {
        return taskAttachment;
    }

    public void setTaskAttachment(String taskAttachment) {
        this.taskAttachment = taskAttachment;
    }

    public String getTaskFromEmployee() {
        return taskFromEmployee;
    }

    public void setTaskFromEmployee(String taskFromEmployee) {
        this.taskFromEmployee = taskFromEmployee;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public java.sql.Date getTaskTerm() {
        return taskTerm;
    }

    public void setTaskTerm(java.sql.Date taskTerm) {
        this.taskTerm = taskTerm;
    }

    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskText='" + taskText + '\'' +
                ", taskAttachment='" + taskAttachment + '\'' +
                ", taskFromEmployee='" + taskFromEmployee + '\'' +
                ", employeeId=" + employeeId +
                ", taskTerm=" + taskTerm +
                '}';
    }
}

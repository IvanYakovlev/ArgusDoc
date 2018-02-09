package model;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

import javax.persistence.*;
import java.io.File;
import java.sql.Time;

@Entity
@Table(name = "TASKS")
public class Task extends RecursiveTreeObject<Task> {

    private Boolean done=false ;
    private Boolean notDone=false;
    private Boolean performed=false;
    private Boolean overdue=false;
    private Boolean canceled=false;
    private BooleanProperty selected ;

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getNotDone() {
        return notDone;
    }

    public void setNotDone(Boolean notDone) {
        this.notDone = notDone;
    }

    public Boolean getPerformed() {
        return performed;
    }

    public void setPerformed(Boolean performed) {
        this.performed = performed;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

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



    @Column(name = "Task_is_letter")
    private int taskIsLetter;          // 0-задача, 1-письмо

    private String oldFile;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public java.sql.Date getTaskTerm() {
        return taskTerm;
    }

    public void setTaskTerm(java.sql.Date taskTerm) {
        this.taskTerm = taskTerm;
    }

    public Time getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Time taskTime) {
        this.taskTime = taskTime;
    }

    public int getStatusTaskId() {
        return statusTaskId;
    }

    public void setStatusTaskId(int statusTaskId) {
        if (statusTaskId==1){
            done=true;
            selected = new SimpleBooleanProperty(done);
        } else if (statusTaskId==2){
            notDone=true;
            selected = new SimpleBooleanProperty(notDone);
        } else if (statusTaskId==3){
            performed=true;
            selected = new SimpleBooleanProperty(performed);
        } else if (statusTaskId==4){
           overdue=true;
           selected = new SimpleBooleanProperty(overdue);
        } else if (statusTaskId==5){
            canceled=true;
            selected = new SimpleBooleanProperty(canceled);
        }

        this.statusTaskId = statusTaskId;
    }

    public String getStatusTaskName() {
        return statusTaskName;
    }

    public void setStatusTaskName(String statusTaskName) {
        this.statusTaskName = statusTaskName;
    }

    public File getTaskAttachmentFile() {
        return taskAttachmentFile;
    }

    public void setTaskAttachmentFile(File taskAttachmentFile) {
        this.taskAttachmentFile = taskAttachmentFile;
    }

    public int getTaskIsLetter() {
        return taskIsLetter;
    }

    public void setTaskIsLetter(int taskIsLetter) {
        this.taskIsLetter = taskIsLetter;
    }

    public String getOldFile() {
        return oldFile;
    }

    public void setOldFile(String oldFile) {
        this.oldFile = oldFile;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskText='" + taskText + '\'' +
                ", taskAttachment='" + taskAttachment + '\'' +
                ", taskFromEmployee='" + taskFromEmployee + '\'' +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", taskTerm=" + taskTerm +
                ", taskTime=" + taskTime +
                ", statusTaskId=" + statusTaskId +
                ", statusTaskName='" + statusTaskName + '\'' +
                ", taskAttachmentFile=" + taskAttachmentFile +
                ", taskIsLetter=" + taskIsLetter +
                ", oldFile='" + oldFile + '\'' +
                '}';
    }



}

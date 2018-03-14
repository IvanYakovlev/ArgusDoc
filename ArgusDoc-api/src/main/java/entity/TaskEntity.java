package entity;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.io.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "TASKS")
public class TaskEntity extends RecursiveTreeObject<TaskEntity> implements Externalizable{






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
    private String statusTaskId;
    private String statusTaskName;

    private File taskAttachmentFile;



    @Column(name = "Task_is_letter")
    private int taskIsLetter;          // 0-задача, 1-письмо

    private String oldFile;


    @Column(name = "Letter_id")
    private int letterId;

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getStatusTaskId() {
        return statusTaskId;
    }

    public void setStatusTaskId(String statusTaskId) {
        this.statusTaskId = statusTaskId;
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
        return "TaskEntity{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskText='" + taskText + '\'' +
                ", taskAttachment='" + taskAttachment + '\'' +
                ", taskFromEmployee='" + taskFromEmployee + '\'' +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", taskTerm=" + taskTerm +
                ", taskTime=" + taskTime +
                ", statusTaskId='" + statusTaskId + '\'' +
                ", statusTaskName='" + statusTaskName + '\'' +
                ", taskAttachmentFile=" + taskAttachmentFile +
                ", taskIsLetter=" + taskIsLetter +
                ", oldFile='" + oldFile + '\'' +
                ", letterId=" + letterId +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getTaskId());
        out.writeObject(getTaskName());
        out.writeObject(getTaskText());
        out.writeObject(getTaskAttachment());
        out.writeObject(getTaskAttachmentFile());
        out.writeInt(getTaskIsLetter());
        out.writeObject(getTaskFromEmployee());
        out.writeObject(getOldFile());
        out.writeInt(getEmployeeId());
        out.writeObject(getEmployeeName());
        out.writeObject(getStatusTaskId());
        out.writeObject(getTaskTerm());
        out.writeObject(getTaskTime());
        out.writeObject(getStatusTaskName());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setTaskId(in.readInt());
        setTaskName((String) in.readObject());
        setTaskText((String) in.readObject());
        setTaskAttachment((String) in.readObject());
        setTaskAttachmentFile((File) in.readObject());
        setTaskIsLetter(in.readInt());
        setTaskFromEmployee((String) in.readObject());
        setOldFile((String) in.readObject());
        setEmployeeId(in.readInt());
        setEmployeeName((String) in.readObject());
        setStatusTaskId((String) in.readObject());
        setTaskTerm((Date) in.readObject());
        setTaskTime((Time) in.readObject());
        setStatusTaskName((String) in.readObject());



    }
}

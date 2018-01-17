package model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TASKS")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Task_id")
    private int taskID;

    @Column(name = "Task_name")
    private String taskName;

    @Column(name = "Task_text")
    private String taskText;

    @Column(name = "Task_attachment")
    private String taskAttachment;

    @Column(name = "Task_from_employee")
    private String taskFromEmployee;

    @Column(name = "Employee_id")
    private int employeeID;

    @Column(name = "Task_term")
    private Date taskTerm;

    @Column(name = "Status_task_id")
    private int statusTaskId;

    private String statusTaskName;





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

    public int getTaskID() {
        return taskID;
    }


    public void setTaskID(int taskID) {
        this.taskID = taskID;
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

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Date getTaskTerm() {
        return taskTerm;
    }

    public void setTaskTerm(Date taskTerm) {
        this.taskTerm = taskTerm;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", taskText='" + taskText + '\'' +
                ", taskAttachment='" + taskAttachment + '\'' +
                ", taskFromEmployee='" + taskFromEmployee + '\'' +
                ", employeeID=" + employeeID +
                ", taskTerm=" + taskTerm +
                '}';
    }
}

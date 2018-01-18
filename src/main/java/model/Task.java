package model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TASKS")
public class Task {

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
    private Date taskTerm;

    @Column(name = "Status_task_id")
    private int statusTaskId;
    private String statusTaskName;


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

    public Date getTaskTerm() {
        return taskTerm;
    }

    public void setTaskTerm(Date taskTerm) {
        this.taskTerm = taskTerm;
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
                ", taskTerm=" + taskTerm +
                '}';
    }
}

package entity;

import javax.persistence.*;
import java.io.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "EVENTS")
public class Event implements Serializable{

    /*Entity класс соответсвующий таблице EVENTS*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Event_id")
    private int eventId;

    @Column(name = "Event_name")
    private String eventName;

    @Column(name = "Event_date")
    private java.sql.Date eventDate;

    @Column(name = "Event_time")
    private java.sql.Time eventTime;

    @Column(name = "Employee_id")
    private int employeeId;

    @Column(name = "Event_periodicity")
    private int eventPeriodicity;


    public int getEventPeriodicity() {
        return eventPeriodicity;
    }

    public void setEventPeriodicity(int eventPeriodicity) {
        this.eventPeriodicity = eventPeriodicity;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getEventTime() {
        return eventTime;
    }

    public void setEventTime(Time eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", eventDate=" + eventDate +
                ", eventTime=" + eventTime +
                ", employeeId=" + employeeId +
                ", eventPeriodicity=" + eventPeriodicity +
                '}';
    }
}

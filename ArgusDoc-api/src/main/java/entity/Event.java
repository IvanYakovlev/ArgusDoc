package entity;

import javax.persistence.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "EVENTS")
public class Event implements Externalizable{
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getEventId());
        out.writeObject(getEventName());
        out.writeObject(getEventDate());
        out.writeObject(getEventTime());
        out.writeInt(getEmployeeId());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setEventId(in.readInt());
        setEventName((String) in.readObject());
        setEventDate((Date) in.readObject());
        setEventTime((Time) in.readObject());
        setEmployeeId(in.readInt());
    }
}

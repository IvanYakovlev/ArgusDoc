package dao;

import dbConnection.DBconnection;
import dialog.ADInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Event;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventDaoImpl implements EventDao {
    DBconnection dBconnection;


    @Override
    public void addEvent(Event event) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO EVENTS(Event_name, Event_date, Event_time, Employee_id) VALUES (?,?,?,?)");
            preparedStatement.setString(1,event.getEventName());
            preparedStatement.setDate(2,event.getEventDate());
            preparedStatement.setTime(3,event.getEventTime());
            preparedStatement.setInt(4,event.getEmployeeId());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeEvent(Event event) {
        this.dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("DELETE FROM EVENTS WHERE Event_id=?");
            preparedStatement.setInt(1,event.getEventId());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Event> listSelectedDayEvent(int authUserId, java.sql.Date selectedDate) {

        this.dBconnection = new DBconnection();
        ObservableList<Event> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM EVENTS WHERE Employee_id='"+authUserId+"' AND Event_date='"+selectedDate+"'");
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventTime(resultSet.getTime("Event_time"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));


                listData.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public ObservableList<Event> listAllEvent(int authUserId) {
        this.dBconnection = new DBconnection();
        ObservableList<Event> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM EVENTS WHERE Employee_id='"+authUserId+"'");
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));
                event.setEventTime(resultSet.getTime("Event_time"));

                listData.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

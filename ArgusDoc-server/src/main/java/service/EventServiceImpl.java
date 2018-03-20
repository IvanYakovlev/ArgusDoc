package service;

import dbConnection.DBconnection;

import entity.Event;


import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {
    DBconnection dBconnection;


    @Override
    public void addEvent(Event event) throws RemoteException, SQLException {
        this.dBconnection = new DBconnection();

            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO EVENTS(Event_name, Event_date, Event_time, Employee_id, Event_periodicity) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1,event.getEventName());
            preparedStatement.setDate(2,event.getEventDate());
            preparedStatement.setTime(3,event.getEventTime());
            preparedStatement.setInt(4,event.getEmployeeId());
            preparedStatement.setInt(5,event.getEventPeriodicity());
            preparedStatement.execute();

    }

    @Override
    public void removeEvent(Event event) throws RemoteException, SQLException {
        this.dBconnection = new DBconnection();

            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("DELETE FROM EVENTS WHERE Event_id=?");
            preparedStatement.setInt(1,event.getEventId());

            preparedStatement.execute();

    }

    @Override
    public List<Event> listSelectedDayEvent(int authUserId, java.sql.Date selectedDate) throws RemoteException{

        this.dBconnection = new DBconnection();
        List<Event> listData = new ArrayList<Event>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM EVENTS WHERE Employee_id='"+authUserId+"' AND Event_date='"+selectedDate+"'");
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventTime(resultSet.getTime("Event_time"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));
                event.setEventPeriodicity(resultSet.getInt("Event_periodicity"));

                listData.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public List<Event> listAllEvent(int authUserId) throws RemoteException{
        this.dBconnection = new DBconnection();
        List<Event> listData = new ArrayList<Event>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM EVENTS WHERE Employee_id='"+authUserId+"'");
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));
                event.setEventTime(resultSet.getTime("Event_time"));
                event.setEventPeriodicity(resultSet.getInt("Event_periodicity"));
                listData.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

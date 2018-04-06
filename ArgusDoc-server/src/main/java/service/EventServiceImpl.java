package service;

import dbConnection.DBconnection;

import entity.Event;


import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventServiceImpl implements EventService {

    @Override
    public void addEvent(Event event) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO EVENTS(Event_name, Event_date, Event_time, Employee_id, Event_periodicity) VALUES (?,?,?,?,?)";

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1,event.getEventName());
            preparedStatement.setDate(2,event.getEventDate());
            preparedStatement.setTime(3,event.getEventTime());
            preparedStatement.setInt(4,event.getEmployeeId());
            preparedStatement.setInt(5,event.getEventPeriodicity());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void updateEvent(Event event) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE EVENTS SET Event_name=?, Event_date=?, Event_time=?, Event_periodicity=? WHERE Event_id=?";

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1,event.getEventName());
            preparedStatement.setDate(2,event.getEventDate());
            preparedStatement.setTime(3,event.getEventTime());
            preparedStatement.setInt(4,event.getEventPeriodicity());
            preparedStatement.setInt(5,event.getEventId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void removeEvent(Event event) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql ="DELETE FROM EVENTS WHERE Event_id=?";

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1,event.getEventId());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Event> listSelectedDayEvent(int authUserId, java.sql.Date selectedDate) throws RemoteException{
        List<Event> listData = new ArrayList<Event>();
        Statement statement = null;
        String sql = "SELECT * FROM EVENTS " +
                "WHERE Employee_id='"+authUserId+"' " +
                "AND Event_date='"+selectedDate+"'";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventTime(resultSet.getTime("Event_time"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));
                event.setEventPeriodicity(resultSet.getInt("Event_periodicity"));
                event.setEventStatus(String.valueOf(resultSet.getInt("Event_status")));
                listData.add(event);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public List<Event> listAllEvent(int authUserId) throws RemoteException{

        List<Event> listData = new ArrayList<Event>();
        Statement statement = null;
        String sql = "SELECT * FROM EVENTS " +
                    "WHERE Employee_id='"+authUserId+"'";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                Event event = new Event();
                event.setEventId(resultSet.getInt("Event_id"));
                event.setEventDate(resultSet.getDate("Event_date"));
                event.setEventName(resultSet.getString("Event_name"));
                event.setEventTime(resultSet.getTime("Event_time"));
                event.setEventPeriodicity(resultSet.getInt("Event_periodicity"));
                event.setEventStatus(String.valueOf(resultSet.getInt("Event_status")));
                listData.add(event);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (statement!=null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return listData;
    }

    @Override
    public void doneEvent(int id) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE EVENTS SET Event_status='1' WHERE Event_id='"+id+"'";

        try {
            preparedStatement =DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (preparedStatement!=null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

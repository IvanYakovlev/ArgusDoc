package service;

import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entity.Access;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessServiceImpl implements AccessService {

    Map<Integer,String> mapAccess = new HashMap<>();
    @Override
    public void addAccess()throws RemoteException {

    }

    @Override
    public void updateAccess() throws RemoteException{

    }

    @Override
    public void removeAccess() throws RemoteException{

    }

    @Override
    public List<Access> listAccess() throws RemoteException{
        List<Access> listData = new ArrayList<Access>();
        Statement statement = null;
        String sql = "SELECT * FROM ACCESS";
        try {
            statement = DBconnection.getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                Access access = new Access();
                access.setAccessId(resultSet.getInt("Access_id"));
                access.setAccesName(resultSet.getString("Access_name"));
                mapAccess.put(access.getAccessId(),access.getAccesName());
                listData.add(access);
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
    public List<String> listAccessName() throws RemoteException{
        List<String> listData = new ArrayList<String >();
        for(Map.Entry<Integer, String> e : mapAccess.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public int getIdAccessByName(String value) throws RemoteException{
        int key=0;
        for(Map.Entry<Integer, String> e : mapAccess.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }
}

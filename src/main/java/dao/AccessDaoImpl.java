package dao;

import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Access;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccessDaoImpl implements AccessDao{
    DBconnection dBconnection;
    Map<Integer,String> mapAccess = new HashMap<>();
    @Override
    public void addAccess() {

    }

    @Override
    public void updateAccess() {

    }

    @Override
    public void removeAccess() {

    }

    @Override
    public ObservableList<Access> listAccess() {
        dBconnection = new DBconnection();
        ObservableList<Access> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM ACCESS";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()){
                Access access = new Access();
                access.setAccessId(resultSet.getInt("Access_id"));
                access.setAccesName(resultSet.getString("Access_name"));
                mapAccess.put(access.getAccessId(),access.getAccesName());
                listData.add(access);
            }

        } catch (SQLException e) {
            System.out.println("Error access list");
        }
        return listData;
    }

    @Override
    public ObservableList<String> listAccessName() {
        ObservableList<String> listData = FXCollections.observableArrayList();
        for(Map.Entry<Integer, String> e : mapAccess.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public int getIdAccessByName(String value) {
        int key=0;
        for(Map.Entry<Integer, String> e : mapAccess.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();// нашли наше значение и возвращаем  ключ
            }
        }
        return key;
    }
}

package dao;

import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Access;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessDaoImpl implements AccessDao{
    DBconnection dBconnection;
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
                listData.add(access);
            }

        } catch (SQLException e) {
            System.out.println("Error access list");
        }
        return listData;
    }

    @Override
    public ObservableList<Integer> listAccessId() {
        dBconnection = new DBconnection();
        ObservableList<Integer> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Access_id FROM ACCESS";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()){

                listData.add(resultSet.getInt("Access_id"));
            }

        } catch (SQLException e) {
            System.out.println("Error access list");
        }
        return listData;
    }
}

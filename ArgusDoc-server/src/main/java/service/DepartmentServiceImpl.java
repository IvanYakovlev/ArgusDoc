package service;


import dialog.ADInfo;
import dbConnection.DBconnection;

import javafx.scene.control.Alert;
import entity.Department;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentServiceImpl implements DepartmentService {

    DBconnection dBconnection;
    Map<Integer, String> mapDepartment = new HashMap<>();

    public Map<Integer, String> getMapDepartment() {
        return mapDepartment;
    }

    public void setMapDepartment(Map<Integer, String> mapDepartment) {
        this.mapDepartment = mapDepartment;
    }

    public void addDepartment(Department department) throws RemoteException {

        this.dBconnection=new DBconnection();

        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Departments(Department_name) VALUES (?)");
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDepartment(Department department) throws RemoteException{
        this.dBconnection =new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE Departments SET Department_name=? WHERE Department_id=?");
            preparedStatement.setInt(2,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void removeDepartment(int id) throws RemoteException{
        dBconnection = new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM Departments WHERE Department_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDepartment.remove(id);
        } catch (SQLException e) {
            ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Удаление невозможно, так как есть пользователи в данном отделе!");
        }
    }

    public List<Department> listDepartments() throws RemoteException{
        dBconnection = new DBconnection();
        List<Department> listData = new ArrayList<Department>();
        try {
            String sql = "SELECT * FROM Departments";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentId(resultSet.getInt("Department_id"));
                department.setDepartmentName(resultSet.getString("Department_name"));
                mapDepartment.put(department.getDepartmentId(),department.getDepartmentName());
                listData.add(department);
            }
        } catch (Exception ex) {
            System.out.println("error list");
        }
        return listData;

    }

    @Override
    public List<String> listDepartmentName() throws RemoteException{
        List<String> listData = new ArrayList<String>();
        for(Map.Entry<Integer, String> e : mapDepartment.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }


    @Override
    public int getIdDepartmentByName(String value) throws RemoteException{
        int key=0;
        for(Map.Entry<Integer, String> e : mapDepartment.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }
}

package service;


import dialog.ADInfo;
import dbConnection.DBconnection;

import javafx.scene.control.Alert;
import entity.Department;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentServiceImpl implements DepartmentService {


    Map<Integer, String> mapDepartment = new HashMap<Integer, String>();

    public Map<Integer, String> getMapDepartment() {
        return mapDepartment;
    }

    public void setMapDepartment(Map<Integer, String> mapDepartment) {
        this.mapDepartment = mapDepartment;
    }

    public void addDepartment(Department department) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO Departments(Department_name) VALUES (?)";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1,department.getDepartmentName());
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
    public void updateDepartment(Department department) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "UPDATE Departments SET Department_name=? WHERE Department_id=?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(2,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
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




    public void removeDepartment(int id) throws RemoteException {
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM Departments WHERE Department_id = ?";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDepartment.remove(id);
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

    public List<Department> listDepartments() throws RemoteException{
        List<Department> listData = new ArrayList<Department>();
        Statement statement = null;
        String sql = "SELECT * FROM Departments";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Department department = new Department();
                department.setDepartmentId(resultSet.getInt("Department_id"));
                department.setDepartmentName(resultSet.getString("Department_name"));
                mapDepartment.put(department.getDepartmentId(),department.getDepartmentName());
                listData.add(department);
            }
            resultSet.close();
        } catch (Exception e) {
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

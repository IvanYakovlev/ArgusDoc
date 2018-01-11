package dao;


import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import model.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DepartmentDaoImpl implements DepartmentDao {
    DBconnection dBconnection;
    Map<Integer, String> mapDepartment = new HashMap<>();

    public Map<Integer, String> getMapDepartment() {
        return mapDepartment;
    }

    public void setMapDepartment(Map<Integer, String> mapDepartment) {
        this.mapDepartment = mapDepartment;
    }

    private void dialog(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType, s);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Информация");
        alert.showAndWait();
    }

    public void addDepartment(Department department) {
        this.dBconnection=new DBconnection();

        try {
            PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Departments(Department_name) VALUES (?)");
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Данный отдел уже существует!");
        }
    }

    @Override
    public void updateDepartment(Department department) {
        this.dBconnection =new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE Departments SET Department_name=? WHERE Department_id=?");
            preparedStatement.setInt(2,department.getDepartmentId());
            preparedStatement.setString(1,department.getDepartmentName());
            preparedStatement.execute();

        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Данный отдел уже существует!");
        }
    }




    public void removeDepartment(int id) {
        dBconnection = new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM Departments WHERE Department_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDepartment.remove(id);
        } catch (SQLException e) {
            dialog(Alert.AlertType.INFORMATION, "Удаление невозможно, так как есть пользователи в данном отделе!");
        }
    }

    public ObservableList<Department> listDepartments() {
        dBconnection = new DBconnection();
        ObservableList<Department> listData = FXCollections.observableArrayList();
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
    public ObservableList<String> listDepartmentName() {
        ObservableList<String> listData = FXCollections.observableArrayList();
        for(Map.Entry<Integer, String> e : mapDepartment.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public Department getDepartmentById(int id)   {
        dBconnection = new DBconnection();
        Department department=null;
        try {
            String sql = "SELECT * FROM Departments WHERE Department_id="+id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);

                department = new Department();
                department.setDepartmentId(resultSet.getInt("Department_id"));
                department.setDepartmentName(resultSet.getString("Department_name"));


            } catch (SQLException e) {
            e.printStackTrace();
        }

        return department;
    }

    @Override
    public int getIddepartmentByName(String value) {
        int key=0;
        for(Map.Entry<Integer, String> e : mapDepartment.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();// нашли наше значение и возвращаем  ключ
            }
        }
        return key;
    }
}

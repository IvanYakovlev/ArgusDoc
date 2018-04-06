package service;

import dialog.ADInfo;
import dbConnection.DBconnection;

import javafx.scene.control.Alert;
import entity.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentServiceImpl implements DocumentService {

    Map<Integer, String> mapDocument = new HashMap<Integer, String>();

    public void addDocument(Document document) throws RemoteException {
                //добавляем запись в таблицу Documents
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO Documents(Document_name,Document_filepath,Department_id) VALUES (?,?,?)";
        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, document.getDocumentName());
            preparedStatement.setString(2, document.getDocumentFilePath());
            preparedStatement.setInt(3, document.getDepartmentId());
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



    public void removeDocument(int id) throws RemoteException {
        //удаляем запись в таблице
        PreparedStatement preparedStatement = null;
        String sql = "DELETE FROM DOCUMENTS WHERE Document_id = ?";

        try {
            preparedStatement = DBconnection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDocument.remove(id);
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
    public List<Document> listDocuments() throws RemoteException{

        List<Document> listData = new ArrayList<Document>();
        Statement statement = null;
        String sql = "SELECT * FROM DOCUMENTS, DEPARTMENTS WHERE DOCUMENTS.Department_id=DEPARTMENTS.Department_id ";

        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Document document = new Document();
                document.setDocumentId((resultSet.getInt("Document_id")));
                document.setDocumentName((resultSet.getString("Document_name")));
                document.setDepartmentId(resultSet.getInt("Department_id"));
                document.setDepartmentName(resultSet.getString("Department_name"));
                document.setDocumentFilePath(resultSet.getString("Document_filepath"));
                mapDocument.put(document.getDocumentId(),document.getDocumentName());
                listData.add(document);
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
    public List<String> listDocumentName() throws RemoteException{
        List<String> listData = new ArrayList<String>();
        for(Map.Entry<Integer, String> e : mapDocument.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public int getIdDocumentByName(String value) throws RemoteException{
        int key=0;
        for(Map.Entry<Integer, String> e : mapDocument.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }

    @Override
    public List<Document> listDocumentsByDepartment(String value) throws RemoteException{
        Statement statement = null;
        List<Document> listData = new ArrayList<Document>();
        String sql = "SELECT * FROM DOCUMENTS WHERE (Department_id =(SELECT Department_id FROM DEPARTMENTS WHERE (Department_name = '"+value+"')))";
        try {
            statement = DBconnection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Document document = new Document();
                document.setDocumentId((resultSet.getInt("Document_id")));
                document.setDocumentName((resultSet.getString("Document_name")));
                document.setDepartmentId(resultSet.getInt("Department_id"));
                document.setDocumentId((resultSet.getInt("Document_id")));
                listData.add(document);
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

}

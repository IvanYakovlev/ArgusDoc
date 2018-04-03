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
    DBconnection dBconnection;
    Map<Integer, String> mapDocument = new HashMap<Integer, String>();

    public void addDocument(Document document) throws IOException, RemoteException, SQLException {

        this.dBconnection = new DBconnection();


                //добавляем запись в таблицу Documents
                PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Documents(Document_name,Document_filepath,Department_id) VALUES (?,?,?)");
                preparedStatement.setString(1, document.getDocumentName());
                preparedStatement.setString(2, document.getDocumentFilePath());
                preparedStatement.setInt(3, document.getDepartmentId());
                preparedStatement.execute();






    }
           /* this.dBconnection = new DBconnection();
            File file;
            FileInputStream fileInputStream = null;
            try {

                file = document.getDocumentFile();
                fileInputStream = new FileInputStream(file);
                long length = file.length();
                if (length>50000000){
                    ADInfo.getAdInfo().dialog(Alert.AlertType.WARNING, "Размер загружаемого документа не должен превышать 50мб!");
                }else {
                    System.out.println(length);
                    PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Documents(Document_name,Document_filepath,Department_id) VALUES (?,?,?)");
                    preparedStatement.setString(1, document.getDocumentName());
                    preparedStatement.setBinaryStream(2, fileInputStream, length);
                    preparedStatement.setInt(3, document.getDepartmentId());
                    preparedStatement.execute();
                }

            } catch (SQLException e) {
                ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Документ с таким названием уже существует!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileInputStream.close();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }*/


    public void removeDocument(int id, String filePath) throws IOException, SQLException {
        dBconnection = new DBconnection();

            //удаляем запись в таблице
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM DOCUMENTS WHERE Document_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDocument.remove(id);

    }

    @Override
    public List<Document> listDocuments() throws RemoteException{


        dBconnection = new DBconnection();
        List<Document> listData = new ArrayList<Document>();
        try {
            String sql = "SELECT * FROM DOCUMENTS, DEPARTMENTS WHERE DOCUMENTS.Department_id=DEPARTMENTS.Department_id ";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
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
        } catch (Exception e) {
            e.printStackTrace();
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
        dBconnection = new DBconnection();
        List<Document> listData = new ArrayList<Document>();
        try {
            String sql = "SELECT * FROM DOCUMENTS WHERE (Department_id =(SELECT Department_id FROM DEPARTMENTS WHERE (Department_name = '"+value+"')))";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                Document document = new Document();
                document.setDocumentId((resultSet.getInt("Document_id")));
                document.setDocumentName((resultSet.getString("Document_name")));
                document.setDepartmentId(resultSet.getInt("Department_id"));
                document.setDocumentId((resultSet.getInt("Document_id")));


                listData.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

}

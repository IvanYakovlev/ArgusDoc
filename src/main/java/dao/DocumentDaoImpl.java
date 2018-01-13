package dao;

import dialog.ADInfo;
import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DocumentDaoImpl implements DocumentDao {
    ADInfo info = new ADInfo();
    DBconnection dBconnection;
    Map<Integer, String> mapDocument = new HashMap<>();

    public void addDocument(Document document) throws IOException {
        if (document.getDocumentFile()==null){
            info.dialog(Alert.AlertType.INFORMATION,"Документ не загружен!");
        }else {
            this.dBconnection = new DBconnection();
            File file;
            FileInputStream fileInputStream = null;
            try {

                file = document.getDocumentFile();
                fileInputStream = new FileInputStream(file);
                PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Documents(Document_name,Document_file,Department_id) VALUES (?,?,?)");
                preparedStatement.setString(1, document.getDocumentName());
                preparedStatement.setBinaryStream(2, fileInputStream);
                preparedStatement.setInt(3, document.getDepartmentId());
                preparedStatement.execute();
            } catch (SQLException e) {
                info.dialog(Alert.AlertType.ERROR, "Документ с таким названием уже существует!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileInputStream.close();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeDocument(int id) {
        dBconnection = new DBconnection();

        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE FROM DOCUMENTS WHERE Document_id = ?");
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            mapDocument.remove(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Document> listDocuments() {
        dBconnection = new DBconnection();
        ObservableList<Document> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM DOCUMENTS, DEPARTMENTS WHERE DOCUMENTS.Department_id=DEPARTMENTS.Department_id ";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                Document document = new Document();
                document.setDocumentId((resultSet.getInt("Document_id")));
                document.setDocumentName((resultSet.getString("Document_name")));
                document.setDepartmentId(resultSet.getInt("Department_id"));
                document.setDepartmentName(resultSet.getString("Department_name"));
                mapDocument.put(document.getDocumentId(),document.getDocumentName());
                listData.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    @Override
    public ObservableList<String> listDocumentName() {
        ObservableList<String> listData = FXCollections.observableArrayList();
        for(Map.Entry<Integer, String> e : mapDocument.entrySet()) {
            listData.add(e.getValue());
        }
        return listData;
    }

    @Override
    public int getIdDocumentByName(String value) {
        int key=0;
        for(Map.Entry<Integer, String> e : mapDocument.entrySet()) {

            if (value.equals(e.getValue())) {
                key = e.getKey();
            }
        }
        return key;
    }
}

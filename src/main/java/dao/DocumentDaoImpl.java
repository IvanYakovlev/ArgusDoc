package dao;

import dialog.ADInfo;
import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Document;

import java.io.*;
import java.sql.*;
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

    @Override
    public ObservableList<Document> listDocumentsByDepartment(String value) {
        dBconnection = new DBconnection();
        ObservableList<Document> listData = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM DOCUMENTS WHERE (Department_id =(SELECT Department_id FROM DEPARTMENTS WHERE (Department_name = '"+value+"')))";
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            while (resultSet.next()) {
                Document document = new Document();
                document.setDocumentName((resultSet.getString("Document_name")));
                listData.add(document);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public void openDocument(int id)  {
        File file1 = new File("E:\\" + mapDocument.get(id));
        if (file1.exists()) {
            try {
                java.awt.Desktop.getDesktop().open(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream FileInputStream = null;
            try {
                dBconnection = new DBconnection();
                String sql = "SELECT Document_file, Document_name FROM DOCUMENTS WHERE Document_id=" + id;
                ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
                if (resultSet.next()) {
                    Blob file = resultSet.getBlob("Document_file");
                    String name = resultSet.getString("Document_name");
                    FileInputStream = file.getBinaryStream();
                    int size = FileInputStream.available();
                    byte b[] = new byte[size];
                    FileInputStream.read(b);

                    try (OutputStream targetFile = new FileOutputStream("E:\\" + name)) {
                        targetFile.write(b);
                        File file2 = new File("E:\\" + name);
                        java.awt.Desktop.getDesktop().open(file2);
                    }
                }
            } catch (SQLException | IOException e) {
                System.out.println("Exception :" + e);
            } finally {

                try {
                    FileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

    @Override
    public void printDocument(int id) {

    }
}

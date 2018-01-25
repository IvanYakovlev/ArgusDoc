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
    DBconnection dBconnection;
    Map<Integer, String> mapDocument = new HashMap<>();

    public void addDocument(Document document) throws IOException {

            this.dBconnection = new DBconnection();
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
                    PreparedStatement preparedStatement = this.dBconnection.connect().prepareStatement("INSERT INTO Documents(Document_name,Document_file,Department_id) VALUES (?,?,?)");
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

    @Override
    public void openDocument(int id)  {
        File file1 = new File("C:\\Temp\\" + mapDocument.get(id));
        if (file1.exists()) {
            try {
                java.awt.Desktop.getDesktop().open(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream inputStream = null;
            try {
                dBconnection = new DBconnection();
                String sql = "SELECT Document_file, Document_name FROM DOCUMENTS WHERE Document_id=" + id;
                ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
                if (resultSet.next()) {
                    Blob file = resultSet.getBlob("Document_file");
                    String name = resultSet.getString("Document_name");
                    inputStream = file.getBinaryStream();
                    int size = inputStream.available();
                    System.out.println(size);
                    byte b[] = new byte[size];


                    try (OutputStream targetFile = new FileOutputStream("C:\\Temp\\" + name)) {
                        while (inputStream.available()>0){
                            inputStream.read(b);
                            targetFile.write(b);
                        }
                        File file2 = new File("C:\\Temp\\" + name);
                        java.awt.Desktop.getDesktop().open(file2);
                    }
                }
            } catch (SQLException | IOException e) {
                System.out.println("Exception :" + e);
            } finally {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

    @Override
    public void printDocument(int id) {
        File file1 = new File("C:\\Temp\\" + mapDocument.get(id));
        if (file1.exists()) {
            try {
                java.awt.Desktop.getDesktop().print(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream inputStream = null;
            try {
                dBconnection = new DBconnection();
                String sql = "SELECT Document_file, Document_name FROM DOCUMENTS WHERE Document_id=" + id;
                ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
                if (resultSet.next()) {
                    Blob file = resultSet.getBlob("Document_file");
                    String name = resultSet.getString("Document_name");
                    inputStream = file.getBinaryStream();
                    int size = inputStream.available();
                    System.out.println(size);
                    byte b[] = new byte[size];


                    try (OutputStream targetFile = new FileOutputStream("C:\\Temp\\" + name)) {
                        while (inputStream.available()>0){
                            inputStream.read(b);
                            targetFile.write(b);
                        }
                        File file2 = new File("C:\\Temp\\" + name);
                        java.awt.Desktop.getDesktop().print(file2);
                    }
                }
            } catch (SQLException | IOException e) {
                System.out.println("Exception :" + e);
            } finally {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }
}

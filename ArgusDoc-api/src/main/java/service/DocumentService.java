package service;

import javafx.collections.ObservableList;
import entity.Document;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface DocumentService extends Remote {

    public void addDocument(Document document) throws IOException, RemoteException, SQLException;
    public void removeDocument(int id, String filePath) throws IOException, SQLException;
    public List<Document> listDocuments() throws RemoteException;
    public List<String> listDocumentName() throws RemoteException;
    public int getIdDocumentByName(String value) throws RemoteException;
    public List<Document> listDocumentsByDepartment(String value) throws RemoteException;
    public void openDocument(int id) throws IOException, SQLException;
    public void printDocument(int id) throws IOException, SQLException;
}

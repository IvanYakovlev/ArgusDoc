package dao;

import javafx.collections.ObservableList;
import model.Document;

import java.io.IOException;
import java.util.List;

public interface DocumentDao {

    public void addDocument(Document document) throws IOException;
    public void removeDocument(int id);
    public ObservableList<Document> listDocuments();
    public ObservableList<String> listDocumentName();
    public int getIdDocumentByName(String value);
    public ObservableList<Document> listDocumentsByDepartment(String value);
    public void openDocument(int id);
    public void printDocument(int id);
}

package dao;

import model.Document;

import java.util.List;

public interface DocumentDao {

    public void addDocument(Document document);
    public void removeDocument(int id);
    public List<Document> listDocuments();
}

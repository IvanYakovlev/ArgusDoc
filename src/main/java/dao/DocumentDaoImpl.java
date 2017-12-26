package dao;

import dbConnection.DBconnection;
import model.Document;

import java.util.List;

public class DocumentDaoImpl implements DocumentDao {
    DBconnection dBconnection;
    public void addDocument(Document document) {
        this.dBconnection = new DBconnection();
    }

    public void removeDocument(int id) {

    }

    public List<Document> listDocuments() {
        return null;
    }
}

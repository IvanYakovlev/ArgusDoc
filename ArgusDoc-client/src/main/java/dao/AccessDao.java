package dao;

import javafx.collections.ObservableList;
import model.Access;

public interface AccessDao {
    public void addAccess();
    public void updateAccess();
    public void removeAccess();
    public ObservableList<Access> listAccess();
    public ObservableList<String> listAccessName();
    public int getIdAccessByName(String value);

}

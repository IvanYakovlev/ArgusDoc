package dao;

import javafx.collections.ObservableList;
import model.Letter;

public interface LetterDao {
    public void addLetter(Letter letter);
    public void removeLetter(int id);
    public void downloadLetter(Letter letter);
    public ObservableList<Letter> listLetter();

}

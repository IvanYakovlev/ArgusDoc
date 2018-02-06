package dao;

import javafx.collections.ObservableList;
import model.Letter;

import java.io.IOException;

public interface LetterDao {
    public void addLetter(Letter letter) throws IOException;
    public void removeLetter(int id);
    public void downloadLetter(Letter letter);
    public void openLetter(int id);
    public ObservableList<Letter> listLetter();

}

package dao;

import dbConnection.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Letter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LetterDaoImpl implements LetterDao {

    DBconnection dBconnection;

    @Override
    public void addLetter(Letter letter) throws IOException {



        dBconnection=new DBconnection();

        try {
            // заносим данные в таблицу Letters
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO LETTERS(Letter_name, Letter_number, Letter_filepath, Letter_password, Letter_date) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, letter.getLetterName());
            preparedStatement.setString(2, letter.getLetterNumber());
            preparedStatement.setString(3, letter.getLetterFilePath());
            preparedStatement.setInt(4, letter.getLetterPassword());
            preparedStatement.setDate(5, letter.getLetterDate());

            preparedStatement.execute();


            //Копируем файл на сервер
            File destFile = new File(letter.getLetterFilePath());
            Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeLetter(int id) {
        dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  LETTERS WHERE Letter_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadLetter(Letter letter) {

    }

    @Override
    public void openLetter(int id) {
        dBconnection = new DBconnection();
        try {
            String sql = "SELECT Letter_filepath FROM LETTERS WHERE Letter_id=" + id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            if (resultSet.next()) {

                String filepath = resultSet.getString("Letter_filepath");

                File file = new File(filepath);
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Letter> listLetter() {
        this.dBconnection = new DBconnection();
        ObservableList<Letter> listData = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM LETTERS ");
            while (resultSet.next()){
                Letter letter= new Letter();
                letter.setLetterId(resultSet.getInt("Letter_id"));
                letter.setLetterName(resultSet.getString("Letter_name"));
                letter.setLetterPassword(resultSet.getInt("Letter_password"));
                letter.setLetterNumber(resultSet.getString("Letter_number"));
                letter.setLetterDate(resultSet.getDate("Letter_date"));
                letter.setLetterFilePath(resultSet.getString("Letter_filepath"));

                listData.add(letter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

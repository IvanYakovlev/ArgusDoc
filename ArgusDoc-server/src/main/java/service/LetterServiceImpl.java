package service;

import dbConnection.DBconnection;
import dialog.ADInfo;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import entity.Letter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LetterServiceImpl implements LetterService {

    DBconnection dBconnection;

    @Override
    public void addLetter(Letter letter) throws IOException, RemoteException, SQLException {



        dBconnection=new DBconnection();



            //Копируем файл на сервер
            File destFile = new File(letter.getLetterFilePath());
            Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());

            // заносим данные в таблицу Letters
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO LETTERS(Letter_name, Letter_number,Letter_date, Letter_filepath, Letter_resolution) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, letter.getLetterName());
            preparedStatement.setString(2, letter.getLetterNumber());
            preparedStatement.setDate(3, letter.getLetterDate());
            preparedStatement.setString(4, letter.getLetterFilePath());
            preparedStatement.setString(5, letter.getLetterResolution());

            preparedStatement.execute();




    }

    @Override
    public void removeLetter(int id, String filePath) throws IOException, SQLException {
        dBconnection = new DBconnection();

            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  LETTERS WHERE Letter_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

            //удаляем файл с сервера
            Path path = Paths.get(filePath);

                Files.delete(path);


    }

    @Override
    public void downloadLetter(Letter letter) throws RemoteException {

    }

    @Override
    public void openLetter(int id) throws IOException, SQLException {
        dBconnection = new DBconnection();

            String sql = "SELECT Letter_filepath FROM LETTERS WHERE Letter_id=" + id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            if (resultSet.next()) {

                String filepath = resultSet.getString("Letter_filepath");

                File file = new File(filepath);

                java.awt.Desktop.getDesktop().open(file);

            }

    }

    @Override
    public List<Letter> listLetter() throws RemoteException {
        this.dBconnection = new DBconnection();
        List<Letter> listData = new ArrayList<Letter>();
        try {
            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM LETTERS ");
            while (resultSet.next()){
                Letter letter= new Letter();
                letter.setLetterId(resultSet.getInt("Letter_id"));
                letter.setLetterName(resultSet.getString("Letter_name"));
                letter.setLetterNumber(resultSet.getString("Letter_number"));
                letter.setLetterDate(resultSet.getDate("Letter_date"));
                letter.setLetterFilePath(resultSet.getString("Letter_filepath"));
                letter.setLetterResolution(resultSet.getString("Letter_resolution"));


                listData.add(letter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

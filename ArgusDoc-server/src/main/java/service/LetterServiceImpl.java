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
    public void addLetter(Letter letter) throws IOException, RemoteException {



        dBconnection=new DBconnection();

        try {

            //Копируем файл на сервер
            File destFile = new File(letter.getLetterFilePath());
            Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());

            // заносим данные в таблицу Letters
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO LETTERS(Letter_name, Letter_number, Letter_filepath, Letter_password, Letter_date) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, letter.getLetterName());
            preparedStatement.setString(2, letter.getLetterNumber());
            preparedStatement.setString(3, letter.getLetterFilePath());
            preparedStatement.setInt(4, letter.getLetterPassword());
            preparedStatement.setDate(5, letter.getLetterDate());
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            //alert.setTitle("Delete File");
            alert.setHeaderText("Письмо с таким именем уже существует! Хотите заменить?");


            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == null) {

            } else if (option.get() == ButtonType.OK) {
                Path path = Paths.get(letter.getLetterFilePath());
                Files.delete(path);
                File destFile = new File(letter.getLetterFilePath());
                Files.copy(letter.getAttachmentFile().toPath(), destFile.toPath());



            } else if (option.get() == ButtonType.CANCEL) {

            } else {

            }
        }

    }

    @Override
    public void removeLetter(int id, String filePath) throws RemoteException{
        dBconnection = new DBconnection();
        try {
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("DELETE  FROM  LETTERS WHERE Letter_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

            //удаляем файл с сервера
            Path path = Paths.get(filePath);
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.out.println("Файл уже удален!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadLetter(Letter letter) throws RemoteException {

    }

    @Override
    public void openLetter(int id) throws RemoteException{
        dBconnection = new DBconnection();
        try {
            String sql = "SELECT Letter_filepath FROM LETTERS WHERE Letter_id=" + id;
            ResultSet resultSet = dBconnection.connect().createStatement().executeQuery(sql);
            if (resultSet.next()) {

                String filepath = resultSet.getString("Letter_filepath");

                File file = new File(filepath);
                try {
                    java.awt.Desktop.getDesktop().open(file);
                }catch (java.lang.IllegalArgumentException e){
                    ADInfo.getAdInfo().dialog(Alert.AlertType.ERROR, "Письмо было удалено с сервера!");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

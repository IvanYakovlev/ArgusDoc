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

        // заносим данные в таблицу Letters
            PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("INSERT INTO LETTERS(Letter_name, Letter_number,Letter_date, Letter_filepath, Letter_resolution,Letter_jurist_number,Letter_jurist_FIO,Letter_jurist_date,Letter_technical_liter,Letter_technical_password,Letter_technical_FIO,Letter_technical_date,Letter_bookkeeping_FIO,Letter_bookkeeping_date,Letter_ORIP_text,Letter_ORIP_FIO,Letter_ORIP_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, letter.getLetterName());
            preparedStatement.setString(2, letter.getLetterNumber());
            preparedStatement.setDate(3, letter.getLetterDate());
            preparedStatement.setString(4, letter.getLetterFilePath());
            preparedStatement.setString(5, letter.getLetterResolution());
            preparedStatement.setString(6, letter.getLetterJuristNumber());
            preparedStatement.setString(7, letter.getLetterJuristFio());
            preparedStatement.setDate(8, letter.getLetterJuristDate());
            preparedStatement.setString(9, letter.getLetterTechnicalLiter());
            preparedStatement.setString(10, letter.getLetterTechnicalPassword());
            preparedStatement.setString(11, letter.getLetterTechnicalFio());
            preparedStatement.setDate(12, letter.getLetterTechnicalDate());
            preparedStatement.setString(13, letter.getLetterBookkeepingFio());
            preparedStatement.setDate(14, letter.getLetterBookkeepingDate());
            preparedStatement.setString(15, letter.getLetterOripText());
            preparedStatement.setString(16, letter.getLetterOripFio());
            preparedStatement.setDate(17, letter.getLetterOripDate());

            preparedStatement.execute();

    }

    @Override
    public void updateLetter(Letter letter) throws IOException, RemoteException, SQLException {
        dBconnection=new DBconnection();
        letter.toString();
        // обновляем данные в таблице Letters
        PreparedStatement preparedStatement = dBconnection.connect().prepareStatement("UPDATE LETTERS SET Letter_name=?, Letter_number=?,Letter_date=?, Letter_filepath=?, Letter_resolution=?,Letter_jurist_number=?,Letter_jurist_FIO=?,Letter_jurist_date=?,Letter_technical_liter=?,Letter_technical_password=?,Letter_technical_FIO=?,Letter_technical_date=?,Letter_bookkeeping_FIO=?,Letter_bookkeeping_date=?,Letter_ORIP_text=?,Letter_ORIP_FIO=?,Letter_ORIP_date=? WHERE Letter_id=?");
        preparedStatement.setString(1, letter.getLetterName());
        preparedStatement.setString(2, letter.getLetterNumber());
        preparedStatement.setDate(3, letter.getLetterDate());
        preparedStatement.setString(4, letter.getLetterFilePath());
        preparedStatement.setString(5, letter.getLetterResolution());
        preparedStatement.setString(6, letter.getLetterJuristNumber());
        preparedStatement.setString(7, letter.getLetterJuristFio());
        preparedStatement.setDate(8, letter.getLetterJuristDate());
        preparedStatement.setString(9, letter.getLetterTechnicalLiter());
        preparedStatement.setString(10, letter.getLetterTechnicalPassword());
        preparedStatement.setString(11, letter.getLetterTechnicalFio());
        preparedStatement.setDate(12, letter.getLetterTechnicalDate());
        preparedStatement.setString(13, letter.getLetterBookkeepingFio());
        preparedStatement.setDate(14, letter.getLetterBookkeepingDate());
        preparedStatement.setString(15, letter.getLetterOripText());
        preparedStatement.setString(16, letter.getLetterOripFio());
        preparedStatement.setDate(17, letter.getLetterOripDate());
        preparedStatement.setInt(18, letter.getLetterId());

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
                letter.setLetterJuristNumber(resultSet.getString("Letter_jurist_number"));
                letter.setLetterJuristFio(resultSet.getString("Letter_jurist_FIO"));
                letter.setLetterJuristDate(resultSet.getDate("Letter_jurist_date"));
                letter.setLetterTechnicalLiter(resultSet.getString("Letter_technical_liter"));
                letter.setLetterTechnicalPassword(resultSet.getString("Letter_technical_password"));
                letter.setLetterTechnicalFio(resultSet.getString("Letter_technical_FIO"));
                letter.setLetterTechnicalDate(resultSet.getDate("Letter_technical_date"));
                letter.setLetterBookkeepingFio(resultSet.getString("Letter_bookkeeping_FIO"));
                letter.setLetterBookkeepingDate(resultSet.getDate("Letter_bookkeeping_date"));
                letter.setLetterOripText(resultSet.getString("Letter_ORIP_text"));
                letter.setLetterOripFio(resultSet.getString("Letter_ORIP_FIO"));
                letter.setLetterOripDate(resultSet.getDate("Letter_ORIP_date"));


                listData.add(letter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listData;
    }

    @Override
    public Letter getLetterById(int id) throws RemoteException {
        this.dBconnection = new DBconnection();
        Letter letter = new Letter();
        try {

            ResultSet resultSet = this.dBconnection.connect().createStatement().executeQuery("SELECT * FROM LETTERS WHERE Letter_id='"+id+"'");
            while (resultSet.next()){

                letter.setLetterId(resultSet.getInt("Letter_id"));
                letter.setLetterName(resultSet.getString("Letter_name"));
                letter.setLetterNumber(resultSet.getString("Letter_number"));
                letter.setLetterDate(resultSet.getDate("Letter_date"));
                letter.setLetterFilePath(resultSet.getString("Letter_filepath"));
                letter.setLetterResolution(resultSet.getString("Letter_resolution"));
                letter.setLetterJuristNumber(resultSet.getString("Letter_jurist_number"));
                letter.setLetterJuristFio(resultSet.getString("Letter_jurist_FIO"));
                letter.setLetterJuristDate(resultSet.getDate("Letter_jurist_date"));
                letter.setLetterTechnicalLiter(resultSet.getString("Letter_technical_liter"));
                letter.setLetterTechnicalPassword(resultSet.getString("Letter_technical_password"));
                letter.setLetterTechnicalFio(resultSet.getString("Letter_technical_FIO"));
                letter.setLetterTechnicalDate(resultSet.getDate("Letter_technical_date"));
                letter.setLetterBookkeepingFio(resultSet.getString("Letter_bookkeeping_FIO"));
                letter.setLetterBookkeepingDate(resultSet.getDate("Letter_bookkeeping_date"));
                letter.setLetterOripText(resultSet.getString("Letter_ORIP_text"));
                letter.setLetterOripFio(resultSet.getString("Letter_ORIP_FIO"));
                letter.setLetterOripDate(resultSet.getDate("Letter_ORIP_date"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return letter;
    }
}

package entity;

import javax.persistence.*;
import java.io.*;
import java.sql.Date;

@Entity
@Table(name = "LETTERS")
public class Letter implements Externalizable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Letter_id")
    private int letterId;

    @Column(name = "Letter_name")
    private String letterName;

    @Column(name = "Letter_number")
    private String letterNumber;

    @Column(name = "Letter_date")
    private java.sql.Date letterDate;

    @Column(name = "Letter_filepath")
    private String letterFilePath;
    private File attachmentFile;

    @Column(name = "Letter_resolution")
    private String letterResolution;

    @Column(name = "Letter_jurist_number")
    private String letterJuristNumber;

    @Column(name = "Letter_jurist_FIO")
    private String letterJuristFio;

    @Column(name = "Letter_jurist_date")
    private java.sql.Date letterJuristDate;

    @Column(name = "Letter_technical_liter")
    private String letterTechnicalLiter;

    @Column(name = "Letter_technical_password")
    private String letterTechnicalPassword;

    @Column(name = "Letter_technical_FIO")
    private String letterTechnicalFio;

    @Column(name = "Letter_technical_date")
    private java.sql.Date letterTechnicalDate;

    @Column(name = "Letter_bookkeeping_FIO")
    private String letterBookkeepingFio;

    @Column(name = "Letter_bookkeeping_date")
    private java.sql.Date letterBookkeepingDate;

    @Column(name = "Letter_ORIP_FIO")
    private String letterOripFio;

    @Column(name = "Letter_ORIP_date")
    private java.sql.Date letterOripDate;

    @Column(name = "Letter_ORIP_text")
    private String letterOripText;


    public String getLetterJuristNumber() {
        return letterJuristNumber;
    }

    public void setLetterJuristNumber(String letterJuristNumber) {
        this.letterJuristNumber = letterJuristNumber;
    }

    public int getLetterId() {
        return letterId;
    }

    public void setLetterId(int letterId) {
        this.letterId = letterId;
    }

    public String getLetterName() {
        return letterName;
    }

    public void setLetterName(String letterName) {
        this.letterName = letterName;
    }

    public String getLetterNumber() {
        return letterNumber;
    }

    public void setLetterNumber(String letterNumber) {
        this.letterNumber = letterNumber;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    public String getLetterFilePath() {
        return letterFilePath;
    }

    public void setLetterFilePath(String letterFilePath) {
        this.letterFilePath = letterFilePath;
    }

    public File getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getLetterResolution() {
        return letterResolution;
    }

    public void setLetterResolution(String letterResolution) {
        this.letterResolution = letterResolution;
    }

    public String getLetterJuristFio() {
        return letterJuristFio;
    }

    public void setLetterJuristFio(String letterJuristFio) {
        this.letterJuristFio = letterJuristFio;
    }

    public Date getLetterJuristDate() {
        return letterJuristDate;
    }

    public void setLetterJuristDate(Date letterJuristDate) {
        this.letterJuristDate = letterJuristDate;
    }

    public String getLetterTechnicalLiter() {
        return letterTechnicalLiter;
    }

    public void setLetterTechnicalLiter(String letterTechnicalLiter) {
        this.letterTechnicalLiter = letterTechnicalLiter;
    }

    public String getLetterTechnicalPassword() {
        return letterTechnicalPassword;
    }

    public void setLetterTechnicalPassword(String letterTechnicalPassword) {
        this.letterTechnicalPassword = letterTechnicalPassword;
    }

    public String getLetterTechnicalFio() {
        return letterTechnicalFio;
    }

    public void setLetterTechnicalFio(String letterTechnicalFio) {
        this.letterTechnicalFio = letterTechnicalFio;
    }

    public Date getLetterTechnicalDate() {
        return letterTechnicalDate;
    }

    public void setLetterTechnicalDate(Date letterTechnicalDate) {
        this.letterTechnicalDate = letterTechnicalDate;
    }

    public String getLetterBookkeepingFio() {
        return letterBookkeepingFio;
    }

    public void setLetterBookkeepingFio(String letterBookkeepingFio) {
        this.letterBookkeepingFio = letterBookkeepingFio;
    }

    public Date getLetterBookkeepingDate() {
        return letterBookkeepingDate;
    }

    public void setLetterBookkeepingDate(Date letterBookkeepingDate) {
        this.letterBookkeepingDate = letterBookkeepingDate;
    }

    public String getLetterOripFio() {
        return letterOripFio;
    }

    public void setLetterOripFio(String letterOripFio) {
        this.letterOripFio = letterOripFio;
    }

    public Date getLetterOripDate() {
        return letterOripDate;
    }

    public void setLetterOripDate(Date letterOripDate) {
        this.letterOripDate = letterOripDate;
    }

    public String getLetterOripText() {
        return letterOripText;
    }

    public void setLetterOripText(String letterOripText) {
        this.letterOripText = letterOripText;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getLetterId());
        out.writeObject(getLetterName());
        out.writeObject(getLetterNumber());
        //out.writeInt(getLetterPassword());
        out.writeObject(getAttachmentFile());
        out.writeObject(getLetterFilePath());
        out.writeObject(getLetterDate());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setLetterId(in.readInt());
        setLetterName((String) in.readObject());
        setLetterNumber((String) in.readObject());
        //setLetterPassword(in.readInt());
        setAttachmentFile((File) in.readObject());
        setLetterFilePath((String) in.readObject());
        setLetterDate((Date) in.readObject());
    }
}

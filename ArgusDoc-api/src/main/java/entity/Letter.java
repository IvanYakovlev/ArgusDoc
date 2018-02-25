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

    @Column(name = "Letter_filepath")
    private String letterFilePath;

    @Column(name = "Letter_password")
    private int letterPassword;

    @Column(name = "Letter_date")
    private java.sql.Date letterDate;

    private File attachmentFile;



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

    public int getLetterPassword() {
        return letterPassword;
    }

    public void setLetterPassword(int letterPassword) {
        this.letterPassword = letterPassword;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getLetterId());
        out.writeObject(getLetterName());
        out.writeObject(getLetterNumber());
        out.writeInt(getLetterPassword());
        out.writeObject(getAttachmentFile());
        out.writeObject(getLetterFilePath());
        out.writeObject(getLetterDate());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setLetterId(in.readInt());
        setLetterName((String) in.readObject());
        setLetterNumber((String) in.readObject());
        setLetterPassword(in.readInt());
        setAttachmentFile((File) in.readObject());
        setLetterFilePath((String) in.readObject());
        setLetterDate((Date) in.readObject());
    }
}

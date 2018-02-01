package model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "LETTERS")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Letter_id")
    private int letterId;

    @Column
    private String letterName;

    @Column
    private int letterNumber;

    @Column
    private String letterFile;

    @Column
    private int letterPassword;

    @Column
    private java.sql.Date letterDate;

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

    public int getLetterNumber() {
        return letterNumber;
    }

    public void setLetterNumber(int letterNumber) {
        this.letterNumber = letterNumber;
    }

    public String getLetterFile() {
        return letterFile;
    }

    public void setLetterFile(String letterFile) {
        this.letterFile = letterFile;
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
}

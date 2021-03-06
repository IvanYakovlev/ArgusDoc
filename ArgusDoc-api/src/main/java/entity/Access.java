package entity;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "ACCESS")
public class Access extends RecursiveTreeObject<Access> implements Serializable{

    /*Entity класс соответсвующий таблице ACCESS*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Access_id")
    private int accessId;

    @Column(name = "Access_name")
    private String accesName;


    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public String getAccesName() {
        return accesName;
    }

    public void setAccesName(String accesName) {
        this.accesName = accesName;
    }

    @Override
    public String toString() {
        return "Access{" +
                "accessId=" + accessId +
                ", accesName='" + accesName + '\'' +
                '}';
    }

}

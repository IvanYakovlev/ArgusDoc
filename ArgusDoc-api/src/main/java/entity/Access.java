package entity;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javax.persistence.*;
import java.io.*;

@Entity
@Table(name = "ACCESS")
public class Access extends RecursiveTreeObject<Access> implements Serializable{
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

/*    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getAccessId());
        out.writeObject(getAccesName());

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setAccesName((String) in.readObject());
        setAccessId(in.readInt());
    }*/
}
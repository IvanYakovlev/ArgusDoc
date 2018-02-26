package entity;
import javax.persistence.*;
import java.io.*;

@Entity
@Table(name ="DOCUMENTS")
public class Document implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Document_id")
    private int documentId;

    @Column(name = "Document_name")
    private String documentName;

    @Column(name = "Document_filepath")
    private String documentFilePath;

    private File documentFile;

    @Column(name = "Department_id")
    private int departmentId;

    private String departmentName;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentFilePath() {
        return documentFilePath;
    }

    public void setDocumentFilePath(String documentFilePath) {
        this.documentFilePath = documentFilePath;
    }

    public File getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(File documentFile) {
        this.documentFile = documentFile;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", documentName='" + documentName + '\'' +
                ", documentFilePath='" + documentFilePath + '\'' +
                ", documentFile=" + documentFile +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }

/*    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getDocumentId());
        out.writeObject(getDocumentName());
        out.writeObject(getDocumentFile());
        out.writeObject(getDocumentFilePath());
        out.writeObject(getDepartmentName());
        out.writeInt(getDepartmentId());

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setDocumentId(in.readInt());
        setDocumentName((String) in.readObject());
        setDocumentFile((File) in.readObject());
        setDocumentFilePath((String) in.readObject());
        setDepartmentName((String) in.readObject());
        setDepartmentId(in.readInt());
    }*/
}

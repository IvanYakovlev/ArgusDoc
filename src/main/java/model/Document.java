package model;
import javax.persistence.*;

@Entity
@Table(name ="DOCUMENTS")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Document_id")
    private int documentId;

    @Column(name = "Document_link")
    private String documentLink;

    @Column(name = "Department_id")
    private int departmentId;


    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId=" + documentId +
                ", documentLink='" + documentLink + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}

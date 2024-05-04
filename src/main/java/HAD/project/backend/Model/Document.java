package HAD.project.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MedicalRecords")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID medicalRecordId;

    private UUID patientId;
    private String recordType; 
    private String lang;
    private String fileName;
    private String fileType;
    private String startDate;
    private String endDate;
    private String display;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] document;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String documentBase64;
}

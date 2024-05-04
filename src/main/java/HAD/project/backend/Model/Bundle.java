package HAD.project.backend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Bundle")
public class Bundle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    
    private String bundleRecordId; 

    @PostPersist
    public void generateBundleRecordId() {
        this.bundleRecordId = "BUNDLE-RECORD-" + id.toString();
    }
    
    private String display; 
    @Column(columnDefinition = "LONGTEXT")
    private String jsonString;

}

package HAD.project.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "TokenToLink")
public class TokenToLink {

    @Id 
    private String patientAbhaAddress;

    @Column(columnDefinition = "LONGTEXT")
    private String accessToken;

    private String expiryTimeStamp;

    private String timeStampOfGeneration;

    private String requestId;
    
}

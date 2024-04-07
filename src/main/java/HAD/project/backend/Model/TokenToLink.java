package HAD.project.backend.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TokenToLink")
public class TokenToLink {

    @Id 
    private String patientAbhaAddress;

    private String accessToken;

    private String timeStampOfGeneration;
    
}

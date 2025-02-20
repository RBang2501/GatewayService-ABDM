package HAD.project.backend.Model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TempStore")
public class Temp {
    
    @Id
    private String requestId;

    @Column(columnDefinition = "LONGTEXT")
    private String jsonString;

}

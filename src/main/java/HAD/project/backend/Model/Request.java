package HAD.project.backend.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity
@Table(name = "Requests")
public class Request {
    
    public enum RequestType {
        FETCH_AUTH, INIT_AUTH, CONFIRM_AUTH, ADD_CARE_CONTEXT, CONTEXT_NOTIFY, SMS_NOTIFY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String requestId;
    private String timeStamp;

    private String abhaAddress;

    private String transactionId;

    @Column(columnDefinition = "LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String accessToken;

    private String requesterType;

    private String requesterId;

    private String purpose;

    private boolean status;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    public static String generateISOTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String isoTimestamp = now.format(formatter);
        return isoTimestamp;
    }

    public Request(String abhaAddress, String requesterType, String requesterId, String purpose) {
        this.abhaAddress = abhaAddress;
        this.requestType = RequestType.FETCH_AUTH;
        this.requestId = UUID.randomUUID().toString();
        this.timeStamp = generateISOTimeStamp();
        this.purpose = purpose;
        this.requesterId = requesterId;
        this.requesterType = requesterType;
    }

    public Request(){};
}

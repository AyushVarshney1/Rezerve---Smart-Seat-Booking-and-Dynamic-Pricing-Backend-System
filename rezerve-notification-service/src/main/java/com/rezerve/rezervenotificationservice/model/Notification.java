package com.rezerve.rezervenotificationservice.model;

import com.rezerve.rezervenotificationservice.model.enums.NotificationStatus;
import com.rezerve.rezervenotificationservice.model.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String email;
    private NotificationStatus status;
    private NotificationType type;

    @CreatedDate
    private Instant createdAt;
}

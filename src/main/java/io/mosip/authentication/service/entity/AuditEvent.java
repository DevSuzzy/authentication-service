package io.mosip.authentication.service.entity;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audit_event")
public class AuditEvent {

    @Id
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "timestamp")
    private Instant timestamp;


}
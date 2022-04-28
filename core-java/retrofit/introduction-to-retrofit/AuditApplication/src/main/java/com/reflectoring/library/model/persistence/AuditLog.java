package com.reflectoring.library.model.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AUDIT_LOG")
@Getter
@Setter
public class AuditLog {

    @Id
    private String id;

    private String request;

    private String response;

    @Enumerated(EnumType.ORDINAL)
    private HttpMethod methodType;

    private String success;

    @Column(name = "CURR_TIMESTAMP")
    private LocalDateTime timestamp;

    public AuditLog() {

    }
}

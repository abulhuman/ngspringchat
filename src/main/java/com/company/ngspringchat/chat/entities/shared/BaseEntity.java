package com.company.ngspringchat.chat.entities.shared;

import jakarta.persistence.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@ToString
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @DateTimeFormat
    private final LocalDateTime created_date = LocalDateTime.now();

    @Setter
    @DateTimeFormat
    private LocalDateTime updated_date = LocalDateTime.now();

    public BaseEntity() {
        this.id = UUID.randomUUID();
    }

    public BaseEntity(UUID uuid) {
        this.id = uuid;
    }
}


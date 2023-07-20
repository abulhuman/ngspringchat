package com.company.ngspringchat.chat.entities;

import com.company.ngspringchat.chat.dtos.FetchMessageDto;
import com.company.ngspringchat.chat.entities.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "Message")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
    private String content;

    @DateTimeFormat
    private LocalDateTime timestamp = super.getCreated_date();
    // TODO: set this to the user when users are implemented
    private String sender;


    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;


    public FetchMessageDto toFetchDto() {
        return new FetchMessageDto(
                super.getId(),
                content,
                timestamp,
                sender);
    }

    public FetchMessageDto toFetchMessageDto() {
        return new FetchMessageDto(
                super.getId(),
                content,
                timestamp,
                sender);
    }
}

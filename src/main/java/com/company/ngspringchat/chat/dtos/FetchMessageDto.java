package com.company.ngspringchat.chat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FetchMessageDto {
    private UUID id;
    private String content;
    private LocalDateTime timestamp;

    // TODO: set this to the user when users are implemented
    private String sender;
}

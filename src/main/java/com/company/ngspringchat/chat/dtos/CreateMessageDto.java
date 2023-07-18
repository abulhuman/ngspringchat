package com.company.ngspringchat.chat.dtos;

import lombok.Data;

@Data
public class CreateMessageDto {
    private String content;

    // TODO: set this to the user when users are implemented
    private String sender;
}

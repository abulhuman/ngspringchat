package com.company.ngspringchat.chat.dtos;

import com.company.ngspringchat.chat.entities.Message;
import lombok.Data;

@Data
public class CreateMessageDto {
    private String content;

    // TODO: set this to the user when users are implemented
    private String sender;

    public Message toEntity() {
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        return message;
    }
}

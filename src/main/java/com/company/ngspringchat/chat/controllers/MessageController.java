package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@RestController
@RequestMapping(path = "messages")
@AllArgsConstructor
public class MessageController {
    @Autowired
    private final MessageService messageService;

    /**
     * Send a message to a room
     *
     * @param createMessageDto Message to send
     * @param roomIdParam      Room ID
     * @Example POST /messages?roomIdParam=123
     * {
     * "content": "Hello World!",
     * "sender": "John Doe",
     * }
     */
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(
            @Valid @RequestBody
            CreateMessageDto createMessageDto,
            @Param("roomIdParam") String roomIdParam
    ) {
        UUID roomId = UUID.fromString(roomIdParam);
        try {
            messageService.sendMessage(createMessageDto, roomId);
        } catch (EntityNotFoundException e) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    "Room with id: '%s' not found".formatted(roomId));
        }
    }
}

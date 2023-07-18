package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.services.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/messages")
@AllArgsConstructor
public class MessageController {
    @Autowired
    private final MessageService messageService;

    @Autowired
    private final ModelMapper modelMapper;

    /**
     * Get all messages in a room
     *
     * @param roomIdParam Room ID
     * @return List of messages
     * @Example GET /messages?roomId=123
     */
    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Message> getMessagesByRoomId(
            @Param("roomIdParam") String roomIdParam
    ) {
        UUID roomId = UUID.fromString(roomIdParam);
        return messageService.getMessagesByRoomId(roomId);
    }

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
        Message message = convertToEntity(createMessageDto);
        messageService.sendMessage(message, roomId);
    }

    private Message convertToEntity(CreateMessageDto createMessageDto) {
        return modelMapper.map(createMessageDto, Message.class);
    }
}

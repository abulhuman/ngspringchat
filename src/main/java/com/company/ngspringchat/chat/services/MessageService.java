package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageService {

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public RoomService roomService;

    public void sendMessage(CreateMessageDto createMessageDto, UUID roomId)
            throws EntityNotFoundException {
        Message message = createMessageDto.toEntity();
        Room room = roomService.getRoomById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        message.setRoom(room);
        messageRepository.save(message);
    }
}

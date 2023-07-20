package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.MessageRepository;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageService {

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public RoomRepository roomRepository;


    public List<Message> getMessagesByRoomId(UUID roomId)
            throws EntityNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        return room.getMessages();
    }

    public void sendMessage(CreateMessageDto createMessageDto, UUID roomId)
            throws EntityNotFoundException {
        Message message = createMessageDto.toEntity();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(EntityNotFoundException::new);
        message.setRoom(room);
        messageRepository.save(message);
    }
}

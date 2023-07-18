package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.ngspringchat.chat.repositories.MessageRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    public MessageRepository messageRepository;

    @Autowired
    public RoomRepository roomRepository;

    public List<Message> getMessagesByRoomId(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        return room.getMessages();
    }

    public void sendMessage(Message message, UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        message.setRoom(room);
        messageRepository.save(message);
    }
}

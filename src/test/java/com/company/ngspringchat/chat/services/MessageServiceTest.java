package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.dtos.CreateRoomDto;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomService roomService;

    private MessageService testSubject;

    @BeforeEach
    void setUp() {
        this.testSubject = new MessageService(messageRepository, roomService);
    }

    @Test
    @DisplayName("Should send message")
    void sendMessage() {
        // given
        CreateMessageDto createMessageDto
                = new CreateMessageDto(
                "Test Message",
                "Test User"
        );
        Message expectedMessage = createMessageDto.toEntity();
        CreateRoomDto createRoomDto = new CreateRoomDto(
                "Testroomcreate",
                "Test Room Create Description",
                "test-update",
                "#000"
        );
        UUID roomId = UUID.randomUUID();
        Room foundRoom = createRoomDto.toEntity();
        foundRoom.setId(roomId);
        given(roomService.getRoomById(roomId))
                .willReturn(Optional.of(foundRoom));
        expectedMessage.setRoom(foundRoom);
        // when
        testSubject.sendMessage(createMessageDto, roomId);
        // then
        verify(messageRepository).save(expectedMessage);
    }
}
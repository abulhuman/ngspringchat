package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.CreateMessageDto;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.MessageRepository;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties",
        properties = {
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                "spring.datasource.url=jdbc:postgresql://localhost:10000/source-it",
                "spring.datasource.username=source-it",
                "spring.datasource.password=source-it",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.show-sql=false",
                "spring.jpa.properties.hibernate.format_sql=false",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
        })
@AutoConfigureMockMvc
@DisplayName("MessageControllerTest - Integration Test")
class MessageIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MessageRepository messageRepository;

    private final Faker faker = new Faker();

    @Test
    @DisplayName("POST /messages?roomIdParam=11111111-1111-1111-1111-111111111111 - 201 Created")
    void shouldSendMessage() throws Exception {
        // given
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String content = faker.lorem().sentence();
        String sender = faker.name().username();
        CreateMessageDto createMessageDto = new CreateMessageDto(
                content,
                sender
        );
        // when
        ResultActions resultActions = mockMvc.perform(
                post("/messages?roomIdParam={roomId}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMessageDto))
        );
        // then
        resultActions
                .andExpect(status().isCreated());
        List<Message> messages
                = roomRepository.findById(roomId)
                .map(Room::getMessages).orElseThrow();
        assertThat(messages.size()).isEqualTo(1);
        assertTrue(messages.stream()
                .anyMatch(m -> m.getContent().equals(content)
                        && m.getSender().equals(sender)));
    }

    // TODO: Complete test for GET /messages?roomIdParam=11111111-1111-1111-1111-111111111111 - 404 Not Found
    @Test
    @DisplayName("GET /messages?roomIdParam=11111111-1111-1111-1111-222222222222 - 404 Not Found")
    @Disabled("Incomplete implementation")
    void shouldReturn404WhenRoomNotFound() throws Exception {
        // given
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-222222222222");
        // when
        ResultActions resultActions = mockMvc.perform(
                post("/messages?roomIdParam={roomId}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateMessageDto("content", "sender")))
        );
        // then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(status().isNotFound());
    }


    // TODO: Add test for GET /messages?roomIdParam=11111111-1111-1111-1111-111111111111 - 400 Bad Request
    // TODO: Add test for GET /messages?roomIdParam=11111111-1111-1111-1111-111111111111 - 500 Internal Server Error (DB error) {HOW?} IDK

    @BeforeEach
    public void setUp() {
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        Room room = new Room(roomId, faker.lorem().sentence(), null, null, null);
        roomRepository.save(room);
    }

    @AfterEach
    public void tearDown() {
        messageRepository.deleteAll();
        roomRepository.deleteAll();
    }
}
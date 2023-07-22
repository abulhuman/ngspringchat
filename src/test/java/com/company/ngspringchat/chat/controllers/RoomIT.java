package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.*;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.MessageRepository;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
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
@DisplayName("RoomControllerTest - Integration Test")
class RoomIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MessageRepository messageRepository;

    private final Faker faker = new Faker();


    @BeforeEach
    public void setUp(TestInfo testInfo) {
        tearDown();
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String name = faker.name().username() + "one";
        String description = faker.lorem().sentence();
        String icon = faker.lorem().word();
        String color = faker.color().hex();
        Room room = new Room(roomId, name, description, icon, color);
        roomRepository.save(room);

        if (testInfo.getDisplayName().equals("GET /rooms/11111111-1111-1111-1111-111111111111/messages - 200 Ok")) {
            String content = "Hello World!";
            String sender = "Test User";
            Message message = new CreateMessageDto( content, sender).toEntity();
            message.setRoom(roomRepository.findById(roomId).orElseThrow());
            messageRepository.save(message);
        }
    }

    @AfterEach
    public void tearDown() {
        messageRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /rooms - 201 Created")
    void canCreateRoom() throws Exception {
        // given
        String word = faker.lorem().word();
        String name = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        String description = faker.lorem().sentence();
        String icon = faker.lorem().word();
        String color = faker.color().hex();
        CreateRoomDto createRoomDto = new CreateRoomDto(name, description, icon, color);
        // when
        ResultActions resultActions = mockMvc.perform(
                post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoomDto))
        );
        // then
        resultActions
                .andExpect(status().isCreated());
        String responseDto = resultActions.andReturn().getResponse().getContentAsString();
        CreateRoomResponseDto createRoomResponseDto = objectMapper.readValue(responseDto, CreateRoomResponseDto.class);
        String roomId = createRoomResponseDto.id();
        assertThat(roomId).isNotNull();
        assertThat(UUID.fromString(roomId)).isInstanceOf(UUID.class);
        assertThat(roomId.split("-").length).isEqualTo(5);
        Room room = roomRepository.findById(UUID.fromString(roomId)).orElseThrow();
        assertThat(room.getName()).isEqualTo(name);
        assertThat(room.getDescription()).isEqualTo(description);
        assertThat(room.getIcon()).isEqualTo(icon);
        assertThat(room.getColor()).isEqualTo(color);
        assertThat(roomId.length()).isEqualTo(36);
    }

    // TODO: Add test for POST /rooms - 400 Bad Request
    // TODO: Add test for POST /rooms - 500 Internal Server Error (DB error) {HOW?} IDK

    @Test
    @DisplayName("GET /rooms/11111111-1111-1111-1111-111111111111 - 200 Ok")
    void canFetchRoomDetail() throws Exception {
        // given
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/rooms/{roomId}", roomId)
        );
        // then
        resultActions
                .andExpect(status().isOk());
        String responseDto = resultActions.andReturn().getResponse().getContentAsString();
        FetchRoomDetailDto fetchRoomDetailDto = objectMapper.readValue(responseDto, FetchRoomDetailDto.class);
        assertThat(fetchRoomDetailDto.getId()).isEqualTo(roomId);
        assertThat(fetchRoomDetailDto.getName()).endsWith("one");
        assertThat(fetchRoomDetailDto.getDescription()).isNotEmpty();
        assertThat(fetchRoomDetailDto.getIcon()).isNotEmpty();
        assertThat(fetchRoomDetailDto.getColor()).isNotEmpty();
        assertThat(fetchRoomDetailDto.getColor()).startsWith("#");
        assertThat(fetchRoomDetailDto.getColor().length()).isEqualTo(7);
        assertThat(fetchRoomDetailDto.getColor()).matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
        assertThat(fetchRoomDetailDto.getMessages()).isInstanceOf(List.class);
        assertThat(fetchRoomDetailDto.getMessages().size()).isEqualTo(0);
    }

    // TODO: Add test for GET /rooms/11111111-1111-1111-1111-111111111111 - 404 Not Found
    // TODO: Add test for GET /rooms/11111111-1111-1111-1111-111111111111 - 500 Internal Server Error (DB error) {HOW?} IDK

    @Test
    @DisplayName("GET /rooms - 200 Ok")
    void canFetchRooms() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/rooms")
        );
        // then
        resultActions
                .andExpect(status().isOk());
        String responseDto = resultActions.andReturn().getResponse().getContentAsString();
        List<FetchRoomDto> fetchRoomsDto = objectMapper.readValue(responseDto, new TypeReference<>() {
        });
        assertThat(fetchRoomsDto).isInstanceOf(List.class);
        assertThat(fetchRoomsDto.size()).isEqualTo(1);
        assertThat(fetchRoomsDto.get(0).getId().toString()).isEqualTo("11111111-1111-1111-1111-111111111111");
        assertThat(fetchRoomsDto.get(0).getName()).endsWith("one");
        assertThat(fetchRoomsDto.get(0).getDescription()).isNotEmpty();
        assertThat(fetchRoomsDto.get(0).getIcon()).isNotEmpty();
        assertThat(fetchRoomsDto.get(0).getColor()).isNotEmpty();
        assertThat(fetchRoomsDto.get(0).getColor()).startsWith("#");
        assertThat(fetchRoomsDto.get(0).getColor().length()).isEqualTo(7);
        assertThat(fetchRoomsDto.get(0).getColor()).matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }

    @Test
    @DisplayName("GET /rooms/11111111-1111-1111-1111-111111111111/messages - 200 Ok")
    void canFetchRoomMessages() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/rooms/{roomId}/messages", "11111111-1111-1111-1111-111111111111")
        );
        // then
        resultActions
                .andExpect(status().isOk());
        String responseDto = resultActions.andReturn().getResponse().getContentAsString();
        List<FetchMessageDto> fetchMessagesDto = objectMapper.readValue(responseDto, new TypeReference<>() {
        });
        assertThat(fetchMessagesDto).isInstanceOf(List.class);
        assertThat(fetchMessagesDto.size()).isEqualTo(1);
        assertThat(UUID.fromString(fetchMessagesDto.get(0).getId().toString())).isInstanceOf(UUID.class);
        assertThat(fetchMessagesDto.get(0).getContent()).isEqualTo("Hello World!");
        assertThat(fetchMessagesDto.get(0).getSender()).isEqualTo("Test User");
        assertThat(fetchMessagesDto.get(0).getTimestamp()).isInstanceOf(LocalDateTime.class);
        assertThat(fetchMessagesDto.get(0).getTimestamp()).isBefore(LocalDateTime.now());
    }

    // TODO: Add test for GET /rooms/11111111-1111-1111-1111-111111111111/messages - 200 Ok (empty list)
    // TODO: Add test for GET /rooms/11111111-1111-1111-1111-111111111111/messages - 400 Bad Request
    // TODO: Add test for GET /rooms/11111111-1111-1111-1111-111111111111/messages - 404 Not Found

    @Test
    @DisplayName("PUT /rooms/11111111-1111-1111-1111-111111111111 - 200 Ok")
    void canUpdateRoom() throws Exception {
        // given
        UUID roomId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String name = "Testroomone";
        String description = "Test Room One Description";
        String icon = "html";
        String color = "#000000";
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(name, description, icon, color);
        // when
        ResultActions resultActions = mockMvc.perform(
                put("/rooms/{roomId}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoomDto))
        );
        // then
        resultActions
                .andExpect(status().isOk());
        Room room = roomRepository.findById(roomId).orElseThrow();
        assertThat(room.getName()).isEqualTo(name);
        assertThat(room.getDescription()).isEqualTo(description);
        assertThat(room.getIcon()).isEqualTo(icon);
        assertThat(room.getColor()).isEqualTo(color);
    }

    // TODO: Add test for PUT /rooms/11111111-1111-1111-1111-111111111111 - 400 Bad Request - Validation Error
    // TODO: Add test for PUT /rooms/11111111-1111-1111-1111-111111111111 - 404 Not Found
    // TODO: Add test for PUT /rooms/11111111-1111-1111-1111-111111111111 - 500 Internal Server Error (DB error) {HOW?} IDK
}
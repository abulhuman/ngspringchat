package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.dtos.CreateRoomDto;
import com.company.ngspringchat.chat.dtos.FetchRoomDetailDto;
import com.company.ngspringchat.chat.dtos.UpdateRoomDto;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    private RoomService testSubject;

    @BeforeEach
    void setUp() {
        this.testSubject = new RoomService(roomRepository, null);
    }

    @Test
    @DisplayName("Should get all rooms")
    void getRooms() {
        // given
        // when
        testSubject.getRooms();
        // then
        verify(roomRepository).findAll();
    }

    @Test
    @DisplayName("Should get room detail by id")
    void getRoomDetailById() {
        // given
        UUID roomId = UUID.randomUUID();
        CreateRoomDto createRoomDto = new CreateRoomDto(
                "Testroom",
                "Test Room Description",
                "test",
                "#000"
        );
        Room expectedRoom = createRoomDto.toEntity();
        expectedRoom.setId(roomId);
        given(roomRepository.findById(roomId))
                .willReturn(Optional.of(expectedRoom));
        // when
        Optional<FetchRoomDetailDto> fetchRoomDetailDtoResult
                =  testSubject.getRoomDetailById(roomId);
        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor
                = ArgumentCaptor.forClass(UUID.class);

        verify(roomRepository)
                .findById(uuidArgumentCaptor.capture());
        UUID capturedRoomId = uuidArgumentCaptor.getValue();
        assertThat(capturedRoomId).isEqualTo(roomId);
        assertThat(fetchRoomDetailDtoResult)
                .isEqualTo(expectedRoom.toFetchDetailDto());
    }

    @Test
    @DisplayName("Should return true if room does not exist by id")
    void doesNotExistById() {
        // given
        UUID roomId = UUID.randomUUID();
        given(roomRepository.existsById(roomId))
                .willReturn(false);
        // when
        boolean doesNotExistById = testSubject.doesNotExistById(roomId);
        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor
                = ArgumentCaptor.forClass(UUID.class);
        verify(roomRepository)
                .existsById(uuidArgumentCaptor.capture());
        UUID capturedUuid = uuidArgumentCaptor.getValue();
        assertThat(capturedUuid).isEqualTo(roomId);
        assertThat(doesNotExistById).isTrue();
    }

    @Test
    @DisplayName("Should return false if room exists by id")
    void doesExistById() {
        // given
        UUID roomId = UUID.randomUUID();
        given(roomRepository.existsById(roomId))
                .willReturn(true);
        // when
        boolean doesExistById = testSubject.doesNotExistById(roomId);
        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor
                = ArgumentCaptor.forClass(UUID.class);
        verify(roomRepository)
                .existsById(uuidArgumentCaptor.capture());
        UUID capturedUuid = uuidArgumentCaptor.getValue();
        assertThat(capturedUuid).isEqualTo(roomId);
        assertThat(doesExistById).isFalse();
    }

    @Test
    @DisplayName("Should create a room")
    void createRoom() {
        // given
        CreateRoomDto createRoomDto = new CreateRoomDto(
                "Testroom",
                "Test Room Description",
                "test",
                "#000"
        );
        // when
        testSubject.createRoom(createRoomDto);
        // then
        ArgumentCaptor<Room> createRoomDtoArgumentCaptor
                = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository)
                .save(createRoomDtoArgumentCaptor
                        .capture());
        Room capturedRoom
                = createRoomDtoArgumentCaptor.getValue();
        assertThat(capturedRoom.getName()).isEqualTo(createRoomDto.getName());
        assertThat(capturedRoom.getDescription()).isEqualTo(createRoomDto.getDescription());
        assertThat(capturedRoom.getIcon()).isEqualTo(createRoomDto.getIcon());
        assertThat(capturedRoom.getColor()).isEqualTo(createRoomDto.getColor());
    }

    @Test
    @DisplayName("Should update a room")
    void updateRoom() {
        // given
        CreateRoomDto createRoomDto = new CreateRoomDto(
                "Testroomcreate",
                "Test Room Create Description",
                "test-update",
                "#000"
        );
        UUID roomId = UUID.randomUUID();
        Room foundRoom = createRoomDto.toEntity();
        foundRoom.setId(roomId);
        given(roomRepository.findById(roomId))
                .willReturn(Optional.of(foundRoom));
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(
                "Testroomupdate",
                "Test Room Update Description",
                "test-update",
                "#000111"
        );
        // when
        testSubject.updateRoom(roomId, updateRoomDto);
        // then
        ArgumentCaptor<Room> updateRoomDtoArgumentCaptor
                = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository)
                .save(updateRoomDtoArgumentCaptor
                        .capture());
        Room capturedRoom
                = updateRoomDtoArgumentCaptor.getValue();
        assertThat(capturedRoom.getId()).isEqualTo(roomId);
        assertThat(capturedRoom.getName()).isEqualTo(updateRoomDto.getName());
        assertThat(capturedRoom.getDescription()).isEqualTo(updateRoomDto.getDescription());
        assertThat(capturedRoom.getIcon()).isEqualTo(updateRoomDto.getIcon());
        assertThat(capturedRoom.getColor()).isEqualTo(updateRoomDto.getColor());
    }

    @Test
    @DisplayName("Should delete room")
    void deleteRoom() {
        // given
        UUID roomId = UUID.randomUUID();
        // when
        testSubject.deleteRoom(roomId);
        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor
                = ArgumentCaptor.forClass(UUID.class);
        verify(roomRepository)
                .deleteById(uuidArgumentCaptor.capture());
        UUID capturedUuid = uuidArgumentCaptor.getValue();
        assertThat(capturedUuid).isEqualTo(roomId);
    }

    @Test
    void getMessagesByRoomId() {
        // given
        UUID roomId = UUID.randomUUID();
        Room room = new Room(
                "Testroom",
                "Test Room Description",
                "test",
                "#000"
        );
        room.setId(roomId);
        given(roomRepository.findById(roomId))
                .willReturn(Optional.of(room));
        // when
        testSubject.getMessages(roomId);
        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor
                = ArgumentCaptor.forClass(UUID.class);
        verify(roomRepository)
                .findById(uuidArgumentCaptor.capture());
        UUID capturedUuid = uuidArgumentCaptor.getValue();
        assertThat(capturedUuid).isEqualTo(roomId);
    }
}
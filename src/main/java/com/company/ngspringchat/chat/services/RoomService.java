package com.company.ngspringchat.chat.services;

import com.company.ngspringchat.chat.dtos.CreateRoomDto;
import com.company.ngspringchat.chat.dtos.FetchRoomDetailDto;
import com.company.ngspringchat.chat.dtos.UpdateRoomDto;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(UUID id) {
        return roomRepository.findById(id);
    }

    public boolean doesNotExistById(UUID id) {
        return !roomRepository.existsById(id);
    }

    public Room createRoom(CreateRoomDto createRoomDto) {
        Room room = createRoomDto.toEntity();
        return roomRepository.save(room);
    }

    public Room updateRoom(UUID roomId, UpdateRoomDto updateRoomDto) {
        Optional<Room> room = getRoomById(roomId);
        if (room.isEmpty()) return null;
        if (updateRoomDto.getName() == null) updateRoomDto.setName(room.get().getName());
        if (updateRoomDto.getDescription() == null) updateRoomDto.setDescription(room.get().getDescription());
        if (updateRoomDto.getIcon() == null) updateRoomDto.setIcon(room.get().getIcon());
        if (updateRoomDto.getColor() == null) updateRoomDto.setColor(room.get().getColor());
        Room updatedRoom = updateRoomDto.toEntity();
        updatedRoom.setId(roomId);
        updatedRoom.setUpdated_date(LocalDateTime.now());
        return roomRepository.save(updatedRoom);
    }

    public void deleteRoom(UUID roomId) {
        roomRepository.deleteById(roomId);
    }

    public Optional<FetchRoomDetailDto> getRoomDetailById(UUID roomId) {
        Optional<Room> room = getRoomById(roomId);
        return room.map(Room::toFetchDetailDto);
    }

}

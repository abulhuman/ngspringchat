package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.CreateRoomDto;
import com.company.ngspringchat.chat.dtos.UpdateRoomDto;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.services.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/rooms")
@AllArgsConstructor
public class RoomController {
    @Autowired
    private final RoomService roomService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    List<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping(path = "/{roomId}")
    Room getRoomById(@PathVariable("roomId") UUID roomId) {
        return roomService.getRoomById(roomId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Room createRoom(@Valid @RequestBody CreateRoomDto createRoomDto) {
        return roomService.createRoom(convertToEntity(createRoomDto));
    }

    @PutMapping(path = "/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Room updateRoom(
            @PathVariable("roomId") UUID roomId,
            @Valid @RequestBody UpdateRoomDto updateRoomDto
    ) {
        if (!roomService.roomExistsById(roomId))
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        return roomService
                .updateRoom(convertToEntity(updateRoomDto, roomId));
    }

    @DeleteMapping(path = "/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable("roomId") UUID roomId) {
        if (!roomService.roomExistsById(roomId)) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        roomService.deleteRoom(roomId);
    }

    private Room convertToEntity(CreateRoomDto createRoomDto) {
        return modelMapper.map(createRoomDto, Room.class);
    }

    private Room convertToEntity(UpdateRoomDto updateRoomDto, UUID id) {
        Room room = roomService.getRoomById(id);
        if (updateRoomDto.getName() == null) updateRoomDto.setName(room.getName());
        if (updateRoomDto.getDescription() == null) updateRoomDto.setDescription(room.getDescription());
        if (updateRoomDto.getIcon() == null) updateRoomDto.setIcon(room.getIcon());
        if (updateRoomDto.getColor() == null) updateRoomDto.setColor(room.getColor());
        Room mappedRoom = modelMapper.map(updateRoomDto, Room.class);
        mappedRoom.setId(id);
        return mappedRoom;
    }
}

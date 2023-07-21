package com.company.ngspringchat.chat.controllers;

import com.company.ngspringchat.chat.dtos.*;
import com.company.ngspringchat.chat.entities.Message;
import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.services.RoomService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "rooms")
@AllArgsConstructor
public class RoomController {
    @Autowired
    private final RoomService roomService;

    /**
     * Get all rooms
     *
     * @return List of rooms
     * @Example GET /rooms
     */
    @GetMapping
    public List<
            FetchRoomDto
            > getRooms() {
        return roomService.getRooms()
                .stream().map(Room::toFetchDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a room by ID
     *
     * @param roomId Room ID
     * @return Room
     * @Example GET /rooms/00000000-0000-0000-0000-000000000
     */
    @GetMapping(path = "{roomId}")
    public FetchRoomDetailDto getRoomById(@PathVariable("roomId") UUID roomId) {
        return roomService.getRoomDetailById(roomId)
                .orElseThrow(() -> new HttpClientErrorException(
                        HttpStatus.NOT_FOUND,
                        "Room with id: '%s' not found".formatted(roomId)
                ));
    }

    /**
     * Get all messages in a room
     *
     * @param roomId Room ID
     * @return List of messages
     * @Example GET /rooms/00000000-0000-0000-0000-000000000/messages
     */
    @GetMapping(path = "{roomId}/messages")
    public List<FetchMessageDto> getRoomMessages(@PathVariable("roomId") UUID roomId) {

        List<Message> messages;
        try {
            messages = roomService.getMessages(roomId);
        } catch (EntityNotFoundException e) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    "Room with id: '%s' not found".formatted(roomId));
        }
        return messages
                .stream().map(Message::toFetchMessageDto)
                .collect(Collectors.toList());
    }

    /**
     * Create a room
     *
     * @param createRoomDto Room to create
     * @return Created room
     * @Example POST /rooms
     * {
     * "name": "Room 1",
     * "description": "This is room 1",
     * "icon": "chat,
     * "color": "#000000"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UUID createRoom(@Valid @RequestBody CreateRoomDto createRoomDto) {
        Room newRoom = roomService.createRoom(createRoomDto);
        return newRoom.getId();
    }

    /**
     * Update a room
     *
     * @param roomId        Room ID
     * @param updateRoomDto Room to update
     * @return Updated room
     * @Example PUT /rooms/00000000-0000-0000-0000-000000000
     * {
     * "name": "Room 1",
     * "description": "This is room 1",
     * "icon": "chat",
     * "color": "#000000"
     * }
     */
    @PutMapping(path = "{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Room updateRoom(
            @PathVariable("roomId") UUID roomId,
            @Valid @RequestBody UpdateRoomDto updateRoomDto
    ) {
        if (roomService.doesNotExistById(roomId))
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    "Room with id: '%s' not found".formatted(roomId));
        return roomService
                .updateRoom(roomId, updateRoomDto);
    }

    /**
     * Delete a room
     *
     * @param roomId Room ID
     * @Example DELETE /rooms/00000000-0000-0000-0000-000000000
     */
    @DeleteMapping(path = "{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable("roomId") UUID roomId) {
        if (roomService.doesNotExistById(roomId))
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    "Room with id: '%s' not found".formatted(roomId));
        roomService.deleteRoom(roomId);
    }
}

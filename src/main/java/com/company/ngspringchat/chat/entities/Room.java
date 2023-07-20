package com.company.ngspringchat.chat.entities;

import com.company.ngspringchat.chat.dtos.FetchRoomDetailDto;
import com.company.ngspringchat.chat.dtos.FetchRoomDto;
import com.company.ngspringchat.chat.entities.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "Room")
@Getter
@Setter
@NoArgsConstructor(force = true)
public class Room extends BaseEntity {
    @Column(unique = true)

    private final String name;
    private final String description;
    private final String icon;
    private final String color;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Message> messages;

    public Room(UUID uuid, String name, String description, String icon, String color) {
        super(uuid);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    public Room(String name, String description, String icon, String color) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    public FetchRoomDto toFetchDto() {
        return new FetchRoomDto(super.getId(), name, description, icon, color);
    }

    public FetchRoomDetailDto toFetchDetailDto() {
        return new FetchRoomDetailDto(
                super.getId(),
                name,
                description,
                icon, color,
                messages.stream()
                        .map(Message::toFetchDto)
                        .collect(Collectors.toList()));
    }
}


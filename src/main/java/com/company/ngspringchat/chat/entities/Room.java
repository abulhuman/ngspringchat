package com.company.ngspringchat.chat.entities;

import com.company.ngspringchat.chat.entities.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Room")
@Getter
@Setter
@RequiredArgsConstructor
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
}


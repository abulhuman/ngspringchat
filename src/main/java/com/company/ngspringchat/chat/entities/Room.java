package com.company.ngspringchat.chat.entities;

import com.company.ngspringchat.chat.entities.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Room")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Room extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    private String icon;
    private String color;

    public Room(UUID uuid, String name, String description, String icon, String color) {
        super(uuid);
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }
}


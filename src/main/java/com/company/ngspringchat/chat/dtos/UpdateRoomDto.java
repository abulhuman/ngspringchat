package com.company.ngspringchat.chat.dtos;

import com.company.ngspringchat.chat.entities.Room;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateRoomDto {
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Name must be a valid capitalized name (e.g. General, Random, etc.)")
    private String name;
    private String description;
    @Pattern(regexp = "^[a-z]+$", message = "Icon must be a valid icon name (e.g. chat, user, etc.)")
    private String icon;
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid hex color" + " (e.g. #XXXXXX or #XXX)")
    private String color;

    public UpdateRoomDto(String name, String description, String icon, String color) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    public Room toEntity() {
        return new Room(name, description, icon, color);
    }
}

package com.company.ngspringchat.chat.dtos;

import com.company.ngspringchat.chat.entities.Room;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreateRoomDto {
    @NotNull
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Name must be a valid capitalized name (e.g. General, Random, etc.)")
    private String name;

    @NotNull
    private String description;

    @NotNull
    @Pattern(regexp = "^[a-z]+$", message = "Icon must be a valid icon name (e.g. chat, user, etc.)")
    private String icon;

    @NotNull
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid hex color" + " (e.g. #XXXXX or #XXX)")
    private String color;

    public Room toEntity() {
        return new Room(name, description, icon, color);
    }
}

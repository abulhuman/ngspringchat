package com.company.ngspringchat.chat.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class FetchRoomDto {
    private UUID id;
    private String name;
    private String description;
    private String icon;
    private String color;
}

package com.company.ngspringchat.chat.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FetchRoomDetailDto {
    private UUID id;
    private String name;
    private String description;
    private String icon;
    private String color;
    private List<FetchMessageDto> messages;
}

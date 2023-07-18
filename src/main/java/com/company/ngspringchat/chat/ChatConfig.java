package com.company.ngspringchat.chat;


import com.company.ngspringchat.chat.entities.Room;
import com.company.ngspringchat.chat.repositories.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * Seed data for the chat application
 */
@Configuration
public class ChatConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            RoomRepository roomRepository
    ) {
        return args -> {
            roomRepository.saveAll(
                    List.of(new Room(
                                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                                    "General",
                                    "Talk about anything. is the general room",
                                    "chat",
                                    "#FF9800"
                            ),
                            new Room(
                                    "Angular",
                                    "Talk about Angular",
                                    "angular",
                                    "#E91E63"
                            ),
                            new Room(
                                    "Spring",
                                    "Talk about Spring",
                                    "spring",
                                    "#2196F3"
                            ),
                            new Room(
                                    "Java",
                                    "Talk about Java",
                                    "java",
                                    "#4CAF50"
                            ),
                            new Room(
                                    "Kotlin",
                                    "Talk about Kotlin",
                                    "kotlin",
                                    "#673AB7"
                            ),
                            new Room(
                                    "Vue",
                                    "Talk about Vue",
                                    "vuejs",
                                    "#4CAF50"
                            ),
                            new Room(
                                    "Python",
                                    "Talk about Python",
                                    "language-python",
                                    "#FFC107"
                            ),
                            new Room(
                                    "Nuxt",
                                    "Talk about Nuxt",
                                    "nuxt",
                                    "#00C58E"

                            ),
                            new Room(
                                    "Node",
                                    "Talk about Node",
                                    "nodejs",
                                    "#8BC34A"
                            )
                    ));
        };
    }
}

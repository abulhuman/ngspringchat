package com.company.ngspringchat.chat.repositories;

import com.company.ngspringchat.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository
        extends JpaRepository<Message, UUID> {}



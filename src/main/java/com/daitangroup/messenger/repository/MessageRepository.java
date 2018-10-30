package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository {
    public void save(Message message);
    public Message findAll();
}

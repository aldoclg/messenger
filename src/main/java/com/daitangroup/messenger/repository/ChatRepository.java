package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Set;

public interface ChatRepository extends MongoRepository<Chat, Long> {

    @Query("{'members.user.email' : ?0}")
    public Set<Chat> findChatByMember(String email);
}

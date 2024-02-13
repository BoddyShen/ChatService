package com.group.ChatService.repository;

import com.group.ChatService.model.Conversation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ConversationRepository extends MongoRepository<Conversation, ObjectId> {
    @Query("{ $or: [ { 'user1Id': ?0, 'user2Id': ?1 }, { 'user1Id': ?1, 'user2Id': ?0 } ] }")
    Optional<Conversation> findConversationByUserIds(ObjectId user1Id, ObjectId user2Id);
}

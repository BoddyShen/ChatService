package com.group.ChatService.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.group.ChatService.model.Message;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {

}
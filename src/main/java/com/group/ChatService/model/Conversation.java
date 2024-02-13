package com.group.ChatService.model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.*;

@Data
@Document(collection = "conversations")
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @Schema(type="string")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private ObjectId user1Id;
    private ObjectId user2Id;
    @DocumentReference
    private List<Message> messages;
    private Instant createdAt = Instant.now();
    private Map<ObjectId, Message> lastMessages = new HashMap<>();
    private Map<ObjectId, Integer> unreadCounts = new HashMap<>();

    public void updateLastMessage(ObjectId senderId, Message message) {
        // Update the last message for this conversation
        this.lastMessages.put(senderId, message);
        // Increment the unread count for the sender
        this.unreadCounts.merge(senderId, 1, Integer::sum);
    }
    public void markAsRead(ObjectId userId) {
        this.unreadCounts.put(userId, 0);
    }

    public Conversation(ObjectId user1Id, ObjectId user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createdAt = Instant.now();
    }
}
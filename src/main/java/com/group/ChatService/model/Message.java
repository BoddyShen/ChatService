package com.group.ChatService.model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Data
@Document(collection = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @Schema(type="string")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId senderId;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId receiverId;
    private String messageText;
    private Instant createdAt = Instant.now();


    public Message(ObjectId senderId, ObjectId receiverId, String messageText) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.createdAt = Instant.now();
    }
}
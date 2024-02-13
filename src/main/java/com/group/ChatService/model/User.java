package com.group.ChatService.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.index.Indexed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Schema(type="string")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;

    @NotBlank
    @Indexed(unique=true)
    @Email
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    private Boolean isActive;
    @JsonIgnore
    private Boolean isDeleted;
    @JsonIgnore
    private Instant createdTime;
    @JsonIgnore
    private Instant updatedTime;

    @DocumentReference
    private Profile profile;

    @DocumentReference
    private List<Match> matches;

    public User(String email, String password) {
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Name and password cannot be null.");
        }
        this.email = email;
        this.password = password;
        this.createdTime = Instant.now(); // UTC
        this.updatedTime = Instant.now();
    }
}

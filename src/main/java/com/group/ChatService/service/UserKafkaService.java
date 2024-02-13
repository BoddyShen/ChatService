package com.group.ChatService.service;

import com.group.ChatService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, CompletableFuture<User>> userFutures = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<List<User>>> usersFutures = new ConcurrentHashMap<>();

    @Autowired
    public UserKafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Producer Method
    public CompletableFuture<User> requestSingleUser(String id) {
        CompletableFuture<User> future = new CompletableFuture<>();
        userFutures.put(id, future);
        kafkaTemplate.send("requestUserById", id);
        return future;
    }

    // Producer Method
    public CompletableFuture<List<User>> requestUsersByIds(String requestId, List<String> ids) {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        usersFutures.put(requestId, future);
        kafkaTemplate.send("requestUsersByIds", requestId); 
        return future;
    }

    // Consumer Method
    @KafkaListener(topics = "responseUserById")
    public void handleUserResponse(String id, User user) {
        CompletableFuture<User> future = userFutures.remove(id);
        if (future != null) {
            future.complete(user);
        } else {
            System.out.println("User not found in KafkaListener");
        }
    }

    // Consumer Method
    @KafkaListener(topics = "responseUsersByIds")
    public void handleUsersResponse(String requestId, List<User> users) {
        CompletableFuture<List<User>> future = usersFutures.remove(requestId);
        if (future != null) {
            future.complete(users);
        } else {
            System.out.println("Users not found in KafkaListener");
        }
    }
}
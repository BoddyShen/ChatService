package com.group.ChatService.controller;

import com.group.ChatService.service.UserKafkaService;
import com.group.ChatService.model.User;
import com.group.ChatService.model.Message;
import com.group.ChatService.model.UserWithConversationData;
import com.group.ChatService.model.Conversation;
import com.group.ChatService.service.ChatService;
import com.group.ChatService.repository.MessageRepository;
import com.group.ChatService.repository.ConversationRepository;
import com.group.ChatService.service.TypeUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/v1/conversation")
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserKafkaService userKafkaService;

    @MessageMapping("/room/{conversationId}/sendMessage")
    public Message sendMessage(@DestinationVariable String conversationId, @Payload Message chatMessage) {
        // Save the message
        Message savedMessage = messageRepository.save(chatMessage);
        // Handle conversation
        chatService.handleConversationForMessage(savedMessage);
        // send to conversation channel
        simpMessagingTemplate.convertAndSend("/room/" + conversationId, savedMessage);
        // send to receiver notification channel
        String receiverId = chatMessage.getReceiverId().toString();
        simpMessagingTemplate.convertAndSend("/room/" + receiverId, savedMessage);

        return savedMessage;
    }

    @GetMapping("/messages")
    public List<Message> getConversationMessages(@RequestParam ObjectId user1Id, @RequestParam ObjectId user2Id) {
        return chatService.getConversationMessages(user1Id, user2Id);
    }

    @GetMapping("/id")
    public ResponseEntity<String> getConversationId(@RequestParam ObjectId user1Id, @RequestParam ObjectId user2Id) {
        String conversationId = chatService.getConversationId(user1Id, user2Id);
        if (conversationId != null) {
            // user2Id is currentChatUser.id
            chatService.markConversationAsRead(user2Id, new ObjectId(conversationId));
            return ResponseEntity.ok(conversationId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/chatting-room-user/{id}")
    public CompletableFuture<ResponseEntity<?>> getSingleUser(@PathVariable String id) {
        return userKafkaService.requestSingleUser(id)
                .thenApply(user -> new UserWithConversationData(user))
                .<ResponseEntity<?>>thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()));
    }

    @GetMapping("/get-matched-users-with-conversation-data-service/{id}")
    public ResponseEntity<List<UserWithConversationData>> getMatchedUsersWithConversationData(@PathVariable String id){

        List<UserWithConversationData> userWithConversationData = chatService.getMatchedUsersWithConversationData(id);
        return ResponseEntity.ok(userWithConversationData);
    }

    @PostMapping("/create-conversation-service")
    public ResponseEntity<Void> createConversation(@RequestBody List<String> userIds) {
        if (userIds == null || userIds.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ObjectId user1Id = new ObjectId(userIds.get(0));
            ObjectId user2Id = new ObjectId(userIds.get(1));
            Conversation newConversation = new Conversation(user1Id, user2Id);
            Conversation savedConversation = conversationRepository.save(newConversation);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception details
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
package com.group.ChatService.service;

import com.group.ChatService.external.client.MatchService;
import com.group.ChatService.external.client.UserService;
import com.group.ChatService.model.User;
import com.group.ChatService.model.Conversation;
import com.group.ChatService.model.Message;
import com.group.ChatService.model.UserWithConversationData;
import com.group.ChatService.repository.ConversationRepository;
import com.group.ChatService.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public void handleConversationForMessage(Message message) {
        ObjectId senderId = message.getSenderId();
        ObjectId receiverId = message.getReceiverId();

        // Find existing conversation or create a new one
        Optional<Conversation> existingConversation = conversationRepository
                .findConversationByUserIds(senderId, receiverId);

        Conversation conversation;
        if (existingConversation.isPresent()) {
            conversation = existingConversation.get();
            conversation.getMessages().add(message);
            // Update last message for both users
            conversation.updateLastMessage(senderId, message);
        } else {
            conversation = new Conversation(senderId, receiverId);
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            conversation.setMessages(messages);

            // Initialize last message and unread count
            conversation.updateLastMessage(senderId, message);
        }
        conversationRepository.save(conversation);
    }

    public List<Message> getConversationMessages(ObjectId user1Id, ObjectId user2Id) {
        Optional<Conversation> foundConversation = conversationRepository.findConversationByUserIds(user1Id, user2Id);
        return foundConversation.map(Conversation::getMessages).orElse(new ArrayList<>());
    }

    public String getConversationId(ObjectId user1Id, ObjectId user2Id) {
        Optional<Conversation> foundConversation = conversationRepository.findConversationByUserIds(user1Id, user2Id);
        return foundConversation.map(conversation -> conversation.getId().toString()).orElse(null);
    }

    public List<UserWithConversationData> getMatchedUsersWithConversationData(String id) {

        List<String> matchedUsersIds = matchService.getMatchedUsersId(id);
        System.out.println(matchedUsersIds);
        List<User> matchedUsers = userService.getUsersByIds(matchedUsersIds);
        System.out.println(matchedUsers);
        ObjectId userId =  TypeUtil.objectIdConverter(id);

        return matchedUsers.stream()
                .map(user -> {
                    UserWithConversationData userWithConversationData = new UserWithConversationData(user);
                    Optional<Conversation> conversation = conversationRepository.findConversationByUserIds(userId, user.getId());
                    conversation.ifPresent(conv -> {
                        Message lastMessage = conv.getLastMessages().get(user.getId());
                        Integer unreadCount = conv.getUnreadCounts().getOrDefault(user.getId(), 0);

                        userWithConversationData.setLastMessage(lastMessage);
                        userWithConversationData.setUnreadCount(unreadCount);
                    });
                    return userWithConversationData;
                })
                .collect(Collectors.toList());
    }

    public void markConversationAsRead(ObjectId userId, ObjectId conversationId) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        conversationOpt.ifPresent(conversation -> {
            conversation.markAsRead(userId);
            conversationRepository.save(conversation);
        });
    }
}
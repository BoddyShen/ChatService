package com.group.ChatService.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithConversationData extends User {
    private Message lastMessage;
    private Integer unreadCount;

    public UserWithConversationData(User user) {
        this.setId(user.getId());
        this.setProfile(user.getProfile());
        this.lastMessage = null;
        this.unreadCount = 0;
    }
}
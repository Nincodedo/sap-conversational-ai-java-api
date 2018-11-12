package com.nincodedo.recast;

import com.nincodedo.recast.conversation.BotConversation;
import com.nincodedo.recast.conversation.BotConversationImpl;

class RecastAPIImpl implements RecastAPI {

    private String token;

    RecastAPI setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public BotConversation startBotConversation() {
        return new BotConversationImpl(this);
    }

    @Override
    public BotConversation startBotConversation(String conversationId) {
        return new BotConversationImpl(this, conversationId);
    }
}

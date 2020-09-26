package dev.nincodedo.sapconversational;

import dev.nincodedo.sapconversational.conversation.BotConversation;
import dev.nincodedo.sapconversational.conversation.BotConversationImpl;

class SAPConversationalAIAPIImpl implements SAPConversationalAIAPI {

    private String token;

    SAPConversationalAIAPI setToken(String token) {
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

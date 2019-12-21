package com.nincodedo.sapconversational;

import com.nincodedo.sapconversational.conversation.BotConversation;

public interface SAPConversationalAIAPI {
    String getToken();

    BotConversation startBotConversation();

    BotConversation startBotConversation(String conversationId);
}

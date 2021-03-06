package dev.nincodedo.sapconversational;

import dev.nincodedo.sapconversational.conversation.BotConversation;

public interface SAPConversationalAIAPI {
    String getToken();

    BotConversation startBotConversation();

    BotConversation startBotConversation(String conversationId);
}

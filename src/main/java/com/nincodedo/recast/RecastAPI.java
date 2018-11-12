package com.nincodedo.recast;

import com.nincodedo.recast.conversation.BotConversation;

public interface RecastAPI {
    String getToken();

    BotConversation startBotConversation();

    BotConversation startBotConversation(String conversationId);
}

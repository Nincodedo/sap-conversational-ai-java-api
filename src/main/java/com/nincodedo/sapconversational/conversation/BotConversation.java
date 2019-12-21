package com.nincodedo.sapconversational.conversation;

import java.util.Optional;

public interface BotConversation {
    Optional<String> getResponse(String input);

    void addParticipants(String... participants);
}

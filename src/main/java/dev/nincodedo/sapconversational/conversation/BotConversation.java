package dev.nincodedo.sapconversational.conversation;

import java.util.Optional;
import java.util.concurrent.Future;

public interface BotConversation {
    Future<Optional<String>> getResponse(String input);

    void addParticipants(String... participants);
}

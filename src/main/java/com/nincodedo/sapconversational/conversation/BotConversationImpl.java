package com.nincodedo.sapconversational.conversation;

import com.nincodedo.sapconversational.SAPConversationalAIAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
@Slf4j
public class BotConversationImpl implements BotConversation {

    private SAPConversationalAIAPI sapConversationalAIAPI;
    private String conversationId;
    private String userInput;
    private List<String> participantList;

    public BotConversationImpl(SAPConversationalAIAPI sapConversationalAIAPI) {
        this.sapConversationalAIAPI = sapConversationalAIAPI;
        this.conversationId = "1";
        participantList = new ArrayList<>();
    }

    public BotConversationImpl(SAPConversationalAIAPI sapConversationalAIAPI, String conversationId) {
        this.sapConversationalAIAPI = sapConversationalAIAPI;
        this.conversationId = conversationId;
        participantList = new ArrayList<>();
    }

    private Optional<StringEntity> buildStringPostEntity() {
        JSONObject jsonObject = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("content", userInput);
        message.put("type", "text");
        jsonObject.put("message", message);
        jsonObject.put("language", "en");
        jsonObject.put("conversation_id", conversationId);
        if (!participantList.isEmpty()) {
            JSONObject memory = new JSONObject();
            for (int i = 0; i < participantList.size(); i++) {
                String participant = participantList.get(i);
                memory.put("participant" + i, participant);
            }
            jsonObject.put("memory", memory);
        }
        try {
            return Optional.of(new StringEntity(jsonObject.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Future<Optional<String>> getResponse(String input){
        CompletableFuture<Optional<String>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            this.userInput = input;
            HttpEntity entity = postResponseRequest();
            completableFuture.complete(getResponseFromEntity(entity));
            return null;
        });
        return completableFuture;
    }

    private HttpEntity postResponseRequest() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.cai.tools.sap/build/v1/dialog");
            post.addHeader("Authorization", "Token " + sapConversationalAIAPI.getToken());
            post.addHeader("Content-Type", "application/json");
            Optional<StringEntity> stringEntityOptional = buildStringPostEntity();
            if (stringEntityOptional.isPresent()) {
                StringEntity stringEntity = stringEntityOptional.get();
                post.setEntity(stringEntity);
                return client.execute(post).getEntity();
            }
        } catch (IOException e) {
            log.error("Failed to post request", e);
        }
        return null;
    }

    private Optional<String> getResponseFromEntity(HttpEntity entity) {
        try {
            JSONObject response = new JSONObject(EntityUtils.toString(entity));
            JSONArray messages = response.getJSONObject("results").getJSONArray("messages");
            if (messages.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(messages.getJSONObject(0).getString("content"));
        } catch (IOException e) {
            log.error("Failed to read response", e);
            return Optional.empty();
        }
    }

    public void addParticipants(String... participants) {
        participantList.addAll(Arrays.asList(participants));
    }
}

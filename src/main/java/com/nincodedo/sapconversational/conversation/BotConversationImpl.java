package com.nincodedo.sapconversational.conversation;

import com.nincodedo.sapconversational.SAPConversationalAIAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
public class BotConversationImpl implements BotConversation {

    private SAPConversationalAIAPI SAPConversationalAIAPI;
    private String conversationId;
    private String userInput;
    private List<String> participantList;

    public BotConversationImpl(SAPConversationalAIAPI SAPConversationalAIAPI) {
        this.SAPConversationalAIAPI = SAPConversationalAIAPI;
        this.conversationId = "1";
        participantList = new ArrayList<>();
    }

    public BotConversationImpl(SAPConversationalAIAPI SAPConversationalAIAPI, String conversationId) {
        this.SAPConversationalAIAPI = SAPConversationalAIAPI;
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

    public Optional<String> getResponse(String input) {
        this.userInput = input;
        String sResponse = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("https://api.cai.tools.sap/build/v1/dialog");
        post.addHeader("Authorization", "Token " + SAPConversationalAIAPI.getToken());
        post.addHeader("Content-Type", "application/json");
        val stringEntityOptional = buildStringPostEntity();
        if (stringEntityOptional.isPresent()) {
            val stringEntity = stringEntityOptional.get();
            post.setEntity(stringEntity);
            JSONObject response;
            try {
                response = new JSONObject(EntityUtils.toString(client.execute(post).getEntity()));
                sResponse = response.getJSONObject("results").getJSONArray("messages").getJSONObject(0).getString("content");
            } catch (IOException e) {
                log.error("Failed to read response", e);
                return Optional.empty();
            }
        }
        return Optional.ofNullable(sResponse);
    }


    public void addParticipants(String... participants) {
        participantList.addAll(Arrays.asList(participants));
    }
}

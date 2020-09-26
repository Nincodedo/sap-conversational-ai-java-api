package dev.nincodedo.sapconversational;

public class SAPConversationalAIAPIBuilder {

    private String token;

    public SAPConversationalAIAPIBuilder(String token) {
        this.token = token;
    }

    public SAPConversationalAIAPI build() {
        return new SAPConversationalAIAPIImpl().setToken(token);
    }
}

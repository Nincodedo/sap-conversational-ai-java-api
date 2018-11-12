package com.nincodedo.recast;

public class RecastAPIBuilder {

    private String token;

    public RecastAPIBuilder(String token) {
        this.token = token;
    }

    public RecastAPI build() {
        return new RecastAPIImpl().setToken(token);
    }
}

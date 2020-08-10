package com.ringcentral.engagevoice;

import com.ringcentral.Utils;
import com.ringcentral.engagevoice.definitions.EngageVoiceToken;
import okhttp3.*;

import java.io.IOException;

public class EngageVoice {
    public String server;
    public OkHttpClient httpClient;

    public EngageVoice(String server, OkHttpClient httpClient) {
        this.server = server;
        if(httpClient == null) {

        } else {
            this.httpClient = httpClient;
        }
    }

    public EngageVoice(String server) {
       this(server, null);
    }

    public EngageVoice() {
        this("https://engage.ringcentral.com", null);
    }

    public EngageVoiceToken authorize(String rcAccessToken) throws IOException, RestException {
        HttpUrl httpUrl = HttpUrl.parse(server).newBuilder("/api/auth/login/rc/accesstoken").build();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("rcAccessToken", rcAccessToken);
        formBodyBuilder.add("rcTokenType", "Bearer");
        Request request = new Request.Builder().url(httpUrl).post(formBodyBuilder.build()).build();
        Response response = httpClient.newCall(request).execute();
        int statusCode = response.code();
        if (statusCode < 200 || statusCode > 299) {
            throw new RestException(response, request);
        }
        return Utils.gson.fromJson(response.body().string(), EngageVoiceToken.class);
    }

    public boolean someLibraryMethod() {
        return true;
    }
}

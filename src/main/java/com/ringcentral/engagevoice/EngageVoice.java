package com.ringcentral.engagevoice;

public class EngageVoice {
    public String server;

    public EngageVoice(String server){
        this.server = server;
    }

    public EngageVoice() {
        this("https://engage.ringcentral.com");
    }

    public boolean someLibraryMethod() {
        return true;
    }
}

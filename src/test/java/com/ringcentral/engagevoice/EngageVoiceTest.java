package com.ringcentral.engagevoice;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.engagevoice.definitions.EngageVoiceToken;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EngageVoiceTest {
    @Test
    public void authorize() throws IOException, RestException, com.ringcentral.engagevoice.RestException {
        Dotenv dotenv = Dotenv.load();
        
        RestClient rc = new RestClient(
                dotenv.get("ENGAGE_VOICE_CLIENT_ID"),
                dotenv.get("ENGAGE_VOICE_CLIENT_SECRET"),
                dotenv.get("ENGAGE_VOICE_RC_SERVER_URL")
        );

        rc.authorize(
                dotenv.get("ENGAGE_VOICE_USERNAME"),
                dotenv.get("ENGAGE_VOICE_EXTENSION"),
                dotenv.get("ENGAGE_VOICE_PASSWORD")
        );

        EngageVoice engageVoice = new EngageVoice(dotenv.get("ENGAGE_VOICE_SERVER_URL"));
        EngageVoiceToken evt = engageVoice.authorize(rc.token.access_token);
        assertNotNull(evt);
        assertNotNull(evt.accessToken);
        assertTrue(evt.accessToken.length() > 0);
    }
}

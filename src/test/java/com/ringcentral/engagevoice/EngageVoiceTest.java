package com.ringcentral.engagevoice;

import com.ringcentral.RestClient;
import com.ringcentral.RestException;
import com.ringcentral.engagevoice.definitions.EngageVoiceToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EngageVoiceTest {
    @Test
    public void authorize() throws IOException, RestException, com.ringcentral.engagevoice.RestException {
        RestClient rc = new RestClient(
                System.getenv("RINGCENTRAL_CLIENT_ID"),
                System.getenv("RINGCENTRAL_CLIENT_SECRET"),
                System.getenv("RINGCENTRAL_SERVER_URL")
        );

        rc.authorize(
                System.getenv("RINGCENTRAL_USERNAME"),
                System.getenv("RINGCENTRAL_EXTENSION"),
                System.getenv("RINGCENTRAL_PASSWORD")
        );
        EngageVoice engageVoice = new EngageVoice();
        EngageVoiceToken evt = engageVoice.authorize(rc.token.access_token);
        assertNotNull(evt);
        assertNotNull(evt.accessToken);
        assertTrue(evt.accessToken.length() > 0);
    }
}

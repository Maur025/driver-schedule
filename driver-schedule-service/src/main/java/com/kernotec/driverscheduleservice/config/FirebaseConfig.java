package com.kernotec.driverscheduleservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kernotec.driverscheduleservice.notification.FirebaseAccountParam;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Slf4j
@Configuration
public class FirebaseConfig {

    private final FirebaseConfigProps firebaseConfigProps;
    private final ObjectMapper objectMapper;

    public FirebaseConfig(FirebaseConfigProps firebaseConfigProps, ResourceLoader resourceLoader,
        ObjectMapper objectMapper)
    {
        this.firebaseConfigProps = firebaseConfigProps;
        this.objectMapper = objectMapper;
    }

    @Bean
    public FirebaseApp firebaseApp() {
        if (!FirebaseApp.getApps()
            .isEmpty())
        {
            return FirebaseApp.getInstance();
        }

        byte[] jsonBytes = getJsonConfigInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(getCredentials(jsonBytes))
            .setConnectTimeout(firebaseConfigProps.getConnectionTimeout())
            .setReadTimeout(firebaseConfigProps.getReadTimeout())
            .build();

        return FirebaseApp.initializeApp(options);
    }

    private byte[] getJsonConfigInputStream() {
        Map<String, String> credentialMap = new HashMap<>();

        credentialMap.put(FirebaseAccountParam.TYPE, firebaseConfigProps.getType());
        credentialMap.put(FirebaseAccountParam.PROJECT_ID, firebaseConfigProps.getProjectId());
        credentialMap.put(
            FirebaseAccountParam.PRIVATE_KEY_ID, firebaseConfigProps.getPrivateKeyId());
        credentialMap.put(
            FirebaseAccountParam.PRIVATE_KEY, firebaseConfigProps.getPrivateKey()
                .replace("\\n", "\n")
        );
        credentialMap.put(FirebaseAccountParam.CLIENT_EMAIL, firebaseConfigProps.getClient_email());
        credentialMap.put(FirebaseAccountParam.CLIENT_ID, firebaseConfigProps.getClientId());
        credentialMap.put(FirebaseAccountParam.AUTH_URI, firebaseConfigProps.getAuthUri());
        credentialMap.put(FirebaseAccountParam.TOKEN_URI, firebaseConfigProps.getTokenUri());
        credentialMap.put(
            FirebaseAccountParam.AUTH_PROVIDER_X590_CERT_URL,
            firebaseConfigProps.getAuthProviderX509CertUrl()
        );
        credentialMap.put(
            FirebaseAccountParam.CLIENT_X590_CERT_URL, firebaseConfigProps.getClientX509CertUrl());
        credentialMap.put(
            FirebaseAccountParam.UNIVERSE_DOMAIN, firebaseConfigProps.getUniverseDomain());

        return getBytesOfMap(credentialMap);
    }

    private byte[] getBytesOfMap(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return new byte[0];
        }

        try {
            return objectMapper.writeValueAsBytes(map);
        } catch (JsonProcessingException ex) {
            log.error("Error serializing Map", ex);
            throw new RuntimeException(ex);
        }
    }

    private GoogleCredentials getCredentials(byte[] jsonBytes) {
        try (InputStream in = new ByteArrayInputStream(jsonBytes)) {
            return GoogleCredentials.fromStream(in);
        } catch (IOException ex) {
            log.info("Error loading Firebase credentials:", ex);
            throw new RuntimeException("Error loading Firebase credentials", ex);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

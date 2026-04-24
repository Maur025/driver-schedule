package com.kernotec.driverscheduleservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Slf4j
@Configuration
public class FirebaseConfig {

    private final FirebaseConfigProps firebaseConfigProps;
    private final ResourceLoader resourceLoader;

    public FirebaseConfig(FirebaseConfigProps firebaseConfigProps, ResourceLoader resourceLoader) {
        this.firebaseConfigProps = firebaseConfigProps;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps()
            .isEmpty())
        {
            return FirebaseApp.getInstance();
        }

        Resource resource = resourceLoader.getResource(firebaseConfigProps.getKeyPath());

        if (!resource.exists()) {
            throw new FileNotFoundException(
                "Firebase key file not found at path: " + resource.getDescription());
        }

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(getCredentials(resource))
            .build();

        return FirebaseApp.initializeApp(options);
    }

    private GoogleCredentials getCredentials(Resource resource) {
        try (InputStream in = resource.getInputStream()) {
            return ServiceAccountCredentials.fromStream(in);
        } catch (IOException e) {
            log.info(
                "Error loading Firebase credentials from resource: {}", resource.getFilename(), e);
            throw new RuntimeException("Error loading Firebase credentials", e);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

package com.exhibition.reserve.service;

import com.exhibition.reserve.model.Congregation;
import com.exhibition.reserve.model.Member;
import com.exhibition.reserve.service.impl.RepositoryServiceImpl;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FcmService {

    @Autowired
    RepositoryService repositoryService;

    private Logger logger = LoggerFactory.getLogger(FcmService.class);

    public FcmService() throws IOException {
        try {
            if (FirebaseApp.getInstance() != null) {
                logger.info("FirebaseApp running");
            }
        } catch (Exception e) {
            File file = null;
            try {
                file = ResourceUtils.getFile("/app/config/reserve/service-account.json");
                FileInputStream serviceAccount = new FileInputStream(file);

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://reserve-c602b.firebaseio.com")
                        .build();

                FirebaseApp.initializeApp(options);
                logger.info("FirebaseApp created!!!");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void sendToTopic(Congregation congregation, String title, String body) throws FirebaseMessagingException {
        // See documentation on defining a message payload.
        List<Member> members = repositoryService.getUserByCongregation(congregation).orElse(new ArrayList<>());
        for (Member member : members) {
            if (member.getFcmToken() != null) {
                Message message = Message.builder()
                        .setWebpushConfig(WebpushConfig.builder()
                                .setNotification(new WebpushNotification(
                                        title,
                                        body))
                                .build())
                        .setToken(member.getFcmToken())
                        .build();

                // Send a message to the devices subscribed to the provided topic.
                String response = FirebaseMessaging.getInstance().send(message);
                // Response is a message ID string.
                System.out.println("Successfully sent message: " + response);
                // [END send_to_topic]
            }
        }
    }
}

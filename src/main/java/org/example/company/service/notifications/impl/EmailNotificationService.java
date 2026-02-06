package org.example.company.service.notifications.impl;

import org.example.company.service.notifications.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@ConditionalOnProperty(name = "company.notification.email.enabled", havingValue = "true")
public class EmailNotificationService implements NotificationService {

    @Override
    public String sendNotification(String message) {
        return "EMAIL_NOTIFICATION " + message;
    }
}

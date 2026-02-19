package org.example.company.service.notification.impl;

import org.example.company.service.notification.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev", "stage"})
@ConditionalOnProperty(name = "company.notification.console.enabled", havingValue = "true", matchIfMissing = true)
public class ConsoleNotificationService implements NotificationService {

    @Override
    public String sendNotification(String message) {
        return "CONSOLE NOTIFICATION = " + message;
    }
}

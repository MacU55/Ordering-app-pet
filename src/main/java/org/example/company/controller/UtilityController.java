package org.example.company.controller;

import org.example.company.service.notification.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility")
@ConditionalOnProperty(name = "company.utility.enabled", havingValue = "true")
public class UtilityController {

    private final NotificationService notificationService;

    public UtilityController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/mainInfo")
    public ResponseEntity<String> getMainInfo(){
        return ResponseEntity.ok("This is the main info from utility controller");
    }


    @GetMapping("/notification/{message}")
    public ResponseEntity<String> getNotifications(@PathVariable String message){
        String messageToSend = notificationService.sendNotification(message);
        return ResponseEntity.ok(messageToSend);
    }

    @GetMapping("/health")
    public ResponseEntity<String> getHealth(){
        return ResponseEntity.ok("UtilityController is up and running");
    }

}

package org.example.company.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.company.service.notification.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility")
@Tag(name = "Utility", description = "Utility endpoints (enabled via company.utility.enabled=true)")
@ConditionalOnProperty(name = "company.utility.enabled", havingValue = "true")
public class UtilityController {

    private final NotificationService notificationService;

    public UtilityController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/mainInfo")
    @Operation(summary = "Get main info")
    public ResponseEntity<String> getMainInfo(){
        return ResponseEntity.ok("This is the main info from utility controller");
    }


    @GetMapping("/notification/{message}")
    @Operation(summary = "Send notification and get response")
    public ResponseEntity<String> getNotifications(
        @Parameter(description = "Notification message") @PathVariable String message){
        String messageToSend = notificationService.sendNotification(message);
        return ResponseEntity.ok(messageToSend);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> getHealth(){
        return ResponseEntity.ok("UtilityController is up and running");
    }

}

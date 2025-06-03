package controller;

import dto.NotificationRequest;
import service.TeamsNotifyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NotificationController {
    
    private final TeamsNotifyService teamsNotifyService;
    
    public NotificationController(TeamsNotifyService teamsNotifyService) {
        this.teamsNotifyService = teamsNotifyService;
    }
    
    @PostMapping("/teams/send")
    public void sendTeamsMessage(@RequestBody NotificationRequest request) {
        teamsNotifyService.sendMessage(request.message());
    }
}
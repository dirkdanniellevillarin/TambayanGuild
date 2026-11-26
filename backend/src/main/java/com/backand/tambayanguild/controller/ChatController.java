package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.ChatMessageEntity;
import com.backand.tambayanguild.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/guilds/{guildId}/chat")
public class ChatController {
 private final ChatService chat;public ChatController(ChatService chat){this.chat=chat;}
 @org.springframework.beans.factory.annotation.Autowired com.backand.tambayanguild.service.CurrentUserService current;
 @GetMapping public List<ChatMessageEntity> history(@PathVariable Long guildId,@RequestParam(required=false) Long userId){current.requireUser(userId);return chat.history(guildId,current.requireUserId());}
 @PostMapping public ChatMessageEntity send(@PathVariable Long guildId,@RequestParam(required=false) Long userId,@RequestBody MessageRequest request){current.requireUser(userId);return chat.send(guildId,current.requireUserId(),request.content());}
 public record MessageRequest(String content){}
}

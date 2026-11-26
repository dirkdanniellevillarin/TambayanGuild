package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.ChatMessageEntity;
import com.backand.tambayanguild.repository.ChatMessageRepository;
import com.backand.tambayanguild.repository.GuildRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service public class ChatService {
 private final ChatMessageRepository messages;private final GuildRepository guilds;private final ProgressService progress;
 public ChatService(ChatMessageRepository messages,GuildRepository guilds,ProgressService progress){this.messages=messages;this.guilds=guilds;this.progress=progress;}
 public List<ChatMessageEntity> history(Long guildId,Long userId){progress.requireMember(guildId,userId);var rows=messages.findTop100ByGuild_IdOrderBySentAtDesc(guildId);java.util.Collections.reverse(rows);return rows;}
 @Transactional public ChatMessageEntity send(Long guildId,Long userId,String content){if(content==null||content.isBlank()||content.length()>2000)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Message must contain 1 to 2000 characters");var member=progress.requireMember(guildId,userId);var guild=guilds.findById(guildId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Guild not found"));return messages.save(new ChatMessageEntity(guild,member.getUser(),content.trim()));}
}

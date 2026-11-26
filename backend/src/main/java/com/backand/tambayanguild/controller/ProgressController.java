package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.UserEntity;
import com.backand.tambayanguild.repository.*;
import com.backand.tambayanguild.service.ProgressService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api")
public class ProgressController {
 private final ProgressService progress; private final UserRepository users; private final GuildMembersRepository memberships;
 private final com.backand.tambayanguild.service.CurrentUserService current;
 public ProgressController(ProgressService progress,UserRepository users,GuildMembersRepository memberships,com.backand.tambayanguild.service.CurrentUserService current){this.progress=progress;this.users=users;this.memberships=memberships;this.current=current;}
 @GetMapping("/users/{userId}/dashboard") public Map<String,Object> dashboard(@PathVariable Long userId){current.requireUser(userId);return progress.dashboard(current.requireUserId());}
 @GetMapping("/users/{userId}/badges") public Object badges(@PathVariable Long userId){current.requireUser(userId);return progress.badges(current.requireUserId());}
 @GetMapping("/leaderboards/global") public Object globalLeaderboard(){current.requireUserId();return progress.leaderboard(users.findAllByOrderByXpDesc());}
 @GetMapping("/leaderboards/guilds/{guildId}") public Object guildLeaderboard(@PathVariable Long guildId){progress.requireMember(guildId,current.requireUserId());var rows=memberships.findByGuild_Id(guildId).stream().map(m->m.getUser()).sorted(Comparator.comparingLong(UserEntity::getXp).reversed()).map(u->{Map<String,Object> row=new LinkedHashMap<>();row.put("userId",u.getId());row.put("username",u.getUsername());row.put("xp",u.getXp());row.put("level",u.getXp()/500+1);row.put("avatarUrl",u.getAvatarUrl());return row;}).toList();return rows;}
 @GetMapping("/users/{userId}/quests") public Object quests(@PathVariable Long userId){current.requireUser(userId);return progress.questProgress(current.requireUserId());}
}

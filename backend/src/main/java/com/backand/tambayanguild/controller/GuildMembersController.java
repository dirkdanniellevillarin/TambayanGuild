package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.GuildMembersEntity;
import com.backand.tambayanguild.service.GuildMembersService;

@RestController
@RequestMapping("/api/guild-members")
public class GuildMembersController {
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;
    @Autowired
    GuildMembersService gmserv;

    public GuildMembersController() {
    }

    // Create
    @PostMapping("/join")
    public Object join(@RequestParam(required=false) String inviteCode,@RequestBody(required=false) JoinRequest request,@RequestParam(required=false) Long userId){if(inviteCode==null&&request!=null)inviteCode=request.inviteCode();if(inviteCode==null||inviteCode.isBlank())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Invite code is required");current.requireUser(userId);return gmserv.memberView(gmserv.joinByInviteCode(inviteCode,current.requireUserId()));}
    public record JoinRequest(String inviteCode){}

    // Read
    @GetMapping("/getAllGuildMembers")
    public List<Object> getAllGuildMembers() {
        current.requireUser(null);
        return gmserv.getAllGuildMembers().stream().map(m->(Object)gmserv.memberView(m)).toList();
    }

    @GetMapping("/getMembersByGuild/{guildId}")
    public List<Object> getMembersByGuild(@PathVariable Long guildId) {
        current.requireUser(null);gmserv.requireMembership(guildId,current.requireUserId());
        return gmserv.getMembersByGuild(guildId).stream().map(m->(Object)gmserv.memberView(m)).toList();
    }

    @GetMapping("/getGuildsByUser/{userId}")
    public List<Object> getGuildsByUser(@PathVariable Long userId) {
        current.requireUser(userId);
        return gmserv.getGuildsByUser(current.requireUserId()).stream().map(m->(Object)gmserv.memberView(m)).toList();
    }

    // Update
    @PutMapping("/putMemberRole")
    public Object putMemberRole(@RequestParam Long id,@RequestParam(required=false) Long actorId, @RequestBody GuildMembersEntity newMemberDetails) {
        current.requireUser(actorId);actorId=current.requireUserId();
        return gmserv.memberView(gmserv.putMemberRole(id,actorId, newMemberDetails));
    }

    // Delete
    @DeleteMapping("/deleteGuildMember/{id}")
    public String deleteGuildMember(@PathVariable Long id,@RequestParam(required=false) Long actorId) {
        current.requireUser(actorId);actorId=current.requireUserId();
        return gmserv.deleteGuildMember(id,actorId);
    }
}

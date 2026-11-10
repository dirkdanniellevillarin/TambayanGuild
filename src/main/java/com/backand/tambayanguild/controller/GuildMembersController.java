package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.GuildMembersEntity;
import com.backand.tambayanguild.service.GuildMembersService;

@RestController
@RequestMapping("/api/guild-members")
public class GuildMembersController {
    @Autowired
    GuildMembersService gmserv;

    public GuildMembersController() {
    }

    // Create
    @PostMapping("/postNewGuildMember")
    public GuildMembersEntity postGuildMember(@RequestBody GuildMembersEntity member) {
        return gmserv.postGuildMember(member);
    }

    // Read
    @GetMapping("/getAllGuildMembers")
    public List<GuildMembersEntity> getAllGuildMembers() {
        return gmserv.getAllGuildMembers();
    }

    @GetMapping("/getMembersByGuild/{guildId}")
    public List<GuildMembersEntity> getMembersByGuild(@PathVariable Long guildId) {
        return gmserv.getMembersByGuild(guildId);
    }

    @GetMapping("/getGuildsByUser/{userId}")
    public List<GuildMembersEntity> getGuildsByUser(@PathVariable Long userId) {
        return gmserv.getGuildsByUser(userId);
    }

    // Update
    @PutMapping("/putMemberRole")
    public GuildMembersEntity putMemberRole(@RequestParam Long id, @RequestBody GuildMembersEntity newMemberDetails) {
        return gmserv.putMemberRole(id, newMemberDetails);
    }

    // Delete
    @DeleteMapping("/deleteGuildMember/{id}")
    public String deleteGuildMember(@PathVariable Long id) {
        return gmserv.deleteGuildMember(id);
    }
}
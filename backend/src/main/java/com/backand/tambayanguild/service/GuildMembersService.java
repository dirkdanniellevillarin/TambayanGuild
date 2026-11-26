package com.backand.tambayanguild.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backand.tambayanguild.entity.GuildMembersEntity;
import com.backand.tambayanguild.repository.GuildMembersRepository;

@Service
public class GuildMembersService {
    @Autowired
    GuildMembersRepository gmrepo;
    @Autowired com.backand.tambayanguild.repository.GuildRepository guilds;
    @Autowired com.backand.tambayanguild.repository.UserRepository users;
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;

    public GuildMembersService() {
    }

    public GuildMembersEntity joinByInviteCode(String code,Long userId){current.requireUser(userId);userId=current.requireUserId();var guild=guilds.findByInviteCodeIgnoreCase(code.trim()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Invalid invite code"));var user=users.findById(userId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"User not found"));if(gmrepo.existsByGuild_IdAndUser_Id(guild.getId(),userId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"Already a guild member");return gmrepo.save(new GuildMembersEntity(guild,user,"MEMBER"));}

    public java.util.Map<String,Object> memberView(GuildMembersEntity member){var view=new java.util.LinkedHashMap<String,Object>();view.put("id",member.getId());view.put("guildId",member.getGuild().getId());view.put("userId",member.getUser().getId());view.put("username",member.getUser().getUsername());view.put("role",member.getRole());view.put("joinedAt",member.getJoinedAt());view.put("currentStreak",member.getCurrentStreak());view.put("bestStreak",member.getBestStreak());return view;}
    public void requireMembership(Long guildId,Long userId){if(!gmrepo.existsByGuild_IdAndUser_Id(guildId,userId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"You must be a guild member");}

    // Create (join a guild)
    public GuildMembersEntity postGuildMember(GuildMembersEntity member) {
        current.requireUser(member.getUser()==null?null:member.getUser().getId());member.setUser(users.findById(current.requireUserId()).orElseThrow());
        member.setId(null);
        if (member.getGuild() == null || member.getUser() == null) {
            throw new RuntimeException("Guild and user are required!");
        }
        if (gmrepo.existsByGuild_IdAndUser_Id(member.getGuild().getId(), member.getUser().getId())) {
            throw new RuntimeException("User " + member.getUser().getId()
                    + " is already in guild " + member.getGuild().getId() + "!");
        }
        return gmrepo.save(member);
    }

    // Read
    public List<GuildMembersEntity> getAllGuildMembers() {
        current.requireUser(null);return gmrepo.findByUser_Id(current.requireUserId());
    }

    public List<GuildMembersEntity> getMembersByGuild(Long guildId) {
        current.requireUser(null);if(gmrepo.findByGuild_IdAndUser_Id(guildId,current.requireUserId()).isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"You must be a guild member");return gmrepo.findByGuild_Id(guildId);
    }

    public List<GuildMembersEntity> getGuildsByUser(Long userId) {
        current.requireUser(userId);return gmrepo.findByUser_Id(current.requireUserId());
    }

    // Update (role assignment)
    public GuildMembersEntity putMemberRole(Long id,Long actorId, GuildMembersEntity newMemberDetails) {
        try {
            current.requireUser(actorId);actorId=current.requireUserId();
            GuildMembersEntity member = gmrepo.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Guild member not found"));
            var actor=gmrepo.findByGuild_IdAndUser_Id(member.getGuild().getId(),actorId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required"));
            if(!"LEADER".equalsIgnoreCase(actor.getRole()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required");
            if("LEADER".equalsIgnoreCase(member.getRole()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Guild leadership cannot be reassigned");
            if(newMemberDetails.getRole()==null||!java.util.Set.of("LEADER","OFFICER","MEMBER").contains(newMemberDetails.getRole().toUpperCase()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Role must be LEADER, OFFICER, or MEMBER");
            member.setRole(newMemberDetails.getRole().toUpperCase());
            return gmrepo.save(member);
        } catch (NoSuchElementException ex) { throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Guild member not found"); }
    }

    // Delete (leave / kick from guild)
    public String deleteGuildMember(Long id,Long actorId) {
        current.requireUser(actorId);actorId=current.requireUserId();
        String msg = "";
        if (gmrepo.findById(id).isPresent()) {
            var member=gmrepo.findById(id).orElseThrow();
            if(!member.getUser().getId().equals(actorId)){var actor=gmrepo.findByGuild_IdAndUser_Id(member.getGuild().getId(),actorId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required"));if(!"LEADER".equalsIgnoreCase(actor.getRole()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required");}
            gmrepo.deleteById(id);
            msg = "Guild member " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }
}

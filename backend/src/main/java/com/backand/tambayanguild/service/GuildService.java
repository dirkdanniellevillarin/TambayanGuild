package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.GuildEntity;
import com.backand.tambayanguild.repository.GuildRepository;
import org.springframework.stereotype.Service;
import java.util.List;

//Implements CRUD operations for GuildEntity entity
@Service
public class GuildService {

    private final GuildRepository guilds;
    private final com.backand.tambayanguild.repository.GuildMembersRepository memberships;
    private final com.backand.tambayanguild.repository.UserRepository users;

    public GuildService(GuildRepository guilds, com.backand.tambayanguild.repository.GuildMembersRepository memberships,
                        com.backand.tambayanguild.repository.UserRepository users) {
        this.guilds = guilds;
        this.memberships = memberships;
        this.users = users;
    }

    public List<GuildEntity> findAll() { 
        return guilds.findAll(); 
    }

    public GuildEntity findById(Long id) { 
        return guilds.findById(id).orElseThrow(() -> new IllegalArgumentException("GuildEntity not found: " + id)); 
    }

    public void requireLeader(Long guildId,Long userId){var member=memberships.findByGuild_IdAndUser_Id(guildId,userId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required"));if(!"LEADER".equalsIgnoreCase(member.getRole()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Guild leader required");}
    public boolean isMember(Long guildId,Long userId){return memberships.existsByGuild_IdAndUser_Id(guildId,userId);}
    public void requireMembership(Long guildId,Long userId){if(!isMember(guildId,userId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"You must be a guild member");}

    @org.springframework.transaction.annotation.Transactional
    public GuildEntity create(GuildEntity GuildEntity) { 
        GuildEntity.setId(null); 
        if (guilds.existsByName(GuildEntity.getName())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Guild name is already taken");
        }
        GuildEntity saved = guilds.save(GuildEntity);
        var owner=users.findById(saved.getOwner().getId()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Owner not found"));
        saved.setOwner(owner);
        memberships.save(new com.backand.tambayanguild.entity.GuildMembersEntity(saved, owner, "LEADER"));
        return saved;
    }

    public com.backand.tambayanguild.entity.GuildMembersEntity joinByInviteCode(String inviteCode, Long userId) {
        var guild = guilds.findByInviteCodeIgnoreCase(inviteCode.trim()).orElseThrow(() ->
                new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Invalid invite code"));
        var user = users.findById(userId).orElseThrow(() ->
                new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
        if (memberships.existsByGuild_IdAndUser_Id(guild.getId(), userId)) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Already a guild member");
        return memberships.save(new com.backand.tambayanguild.entity.GuildMembersEntity(guild, user, "MEMBER"));
    }

    public GuildEntity findByInviteCode(String inviteCode){return guilds.findByInviteCodeIgnoreCase(inviteCode.trim()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Invalid invite code"));}
    public java.util.Map<String,Object> guildView(GuildEntity guild){var view=new java.util.LinkedHashMap<String,Object>();view.put("id",guild.getId());view.put("name",guild.getName());view.put("description",guild.getDescription());view.put("inviteCode",guild.getInviteCode());view.put("ownerId",guild.getOwner().getId());view.put("createdAt",guild.getCreatedAt());return view;}

    public GuildEntity update(Long id, GuildEntity input) {
        GuildEntity GuildEntity = findById(id);
        if(input.getName()==null||input.getName().isBlank())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Guild name is required");
        if (guilds.existsByNameAndIdNot(input.getName(), id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Guild name is already taken");
        }
        GuildEntity.setName(input.getName()); GuildEntity.setDescription(input.getDescription());
        return guilds.save(GuildEntity);
    }
    
    @org.springframework.transaction.annotation.Transactional
    public void delete(Long id) { 
        memberships.deleteAll(memberships.findByGuild_Id(id));
        guilds.delete(findById(id)); 
    }
}

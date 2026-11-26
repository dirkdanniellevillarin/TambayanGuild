package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.GuildEntity;
import com.backand.tambayanguild.service.GuildService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//REST endpoints setters and getters for GuildEntity entity
@RestController
@RequestMapping("/api/guilds")

public class GuildController {

    private final GuildService service;
    private final com.backand.tambayanguild.service.CurrentUserService current;

    public GuildController(GuildService service,com.backand.tambayanguild.service.CurrentUserService current) { 
        this.service = service;this.current=current;
    }

    @GetMapping 
    public List<Object> getAll() {
        Long userId=current.requireUserId();return service.findAll().stream().filter(g->service.isMember(g.getId(),userId)).map(g -> (Object)service.guildView(g)).toList(); 
    }

    @GetMapping("/{id}") 
    public Object getOne(@PathVariable Long id) { 
        current.requireUser(null);service.requireMembership(id,current.requireUserId());
        return service.guildView(service.findById(id)); 
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED) 
    public Object create(@RequestBody GuildEntity GuildEntity) { 
        current.requireUser(GuildEntity.getOwner()==null?null:GuildEntity.getOwner().getId());
        GuildEntity.setOwner(new com.backand.tambayanguild.entity.UserEntity());GuildEntity.getOwner().setId(current.requireUserId());
        return service.guildView(service.create(GuildEntity)); 
    }

    @PostMapping("/join")
    public Object join(@RequestParam(required=false) String inviteCode,@RequestBody(required=false) JoinRequest request,@RequestParam(required=false) Long userId) {
        if(inviteCode==null&&request!=null)inviteCode=request.inviteCode();if(inviteCode==null||inviteCode.isBlank())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Invite code is required");
        current.requireUser(userId);var member=service.joinByInviteCode(inviteCode,current.requireUserId());return java.util.Map.of("id",member.getId(),"guildId",member.getGuild().getId(),"userId",member.getUser().getId(),"role",member.getRole(),"joinedAt",member.getJoinedAt());
    }
    public record JoinRequest(String inviteCode){}

    @GetMapping("/by-invite/{inviteCode}") public Object findByInvite(@PathVariable String inviteCode){Long id=current.requireUserId();var g=service.findByInviteCode(inviteCode);return java.util.Map.of("id",g.getId(),"name",g.getName(),"description",g.getDescription(),"inviteCode",g.getInviteCode(),"member",service.isMember(g.getId(),id));}

    @PutMapping("/{id}") 
    public Object update(@PathVariable Long id,@RequestParam(required=false) Long actorId, @RequestBody GuildEntity GuildEntity) { 
            current.requireUser(actorId);service.requireLeader(id,current.requireUserId());GuildEntity.setOwner(new com.backand.tambayanguild.entity.UserEntity());GuildEntity.getOwner().setId(current.requireUserId());
            return service.guildView(service.update(id, GuildEntity)); 
    }

    @DeleteMapping("/{id}") 
    
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id,@RequestParam(required=false) Long actorId) {
        current.requireUser(actorId);service.requireLeader(id,current.requireUserId());
        service.delete(id); 
        }
}

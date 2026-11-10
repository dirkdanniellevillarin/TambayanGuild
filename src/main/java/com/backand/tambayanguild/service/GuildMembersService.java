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

    public GuildMembersService() {
    }

    // Create (join a guild)
    public GuildMembersEntity postGuildMember(GuildMembersEntity member) {
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
        return gmrepo.findAll();
    }

    public List<GuildMembersEntity> getMembersByGuild(Long guildId) {
        return gmrepo.findByGuild_Id(guildId);
    }

    public List<GuildMembersEntity> getGuildsByUser(Long userId) {
        return gmrepo.findByUser_Id(userId);
    }

    // Update (role assignment)
    public GuildMembersEntity putMemberRole(Long id, GuildMembersEntity newMemberDetails) {
        try {
            GuildMembersEntity member = gmrepo.findById(id).get();
            member.setRole(newMemberDetails.getRole());
            return gmrepo.save(member);
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Guild member " + id + " does not exist!");
        }
    }

    // Delete (leave / kick from guild)
    public String deleteGuildMember(Long id) {
        String msg = "";
        if (gmrepo.findById(id).isPresent()) {
            gmrepo.deleteById(id);
            msg = "Guild member " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }
}
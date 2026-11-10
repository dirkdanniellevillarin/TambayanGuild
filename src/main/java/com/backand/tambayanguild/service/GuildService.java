package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.Guild;
import com.backand.tambayanguild.repository.GuildRepository;
import org.springframework.stereotype.Service;
import java.util.List;

//Implements CRUD operations for Guild entity
@Service
public class GuildService {

    private final GuildRepository guilds;

    public GuildService(GuildRepository guilds) { 
        this.guilds = guilds; 
    }

    public List<Guild> findAll() { 
        return guilds.findAll(); 
    }

    public Guild findById(Long id) { 
        return guilds.findById(id).orElseThrow(() -> new IllegalArgumentException("Guild not found: " + id)); 
    }

    public Guild create(Guild guild) { 
        guild.setId(null); return guilds.save(guild); 
    }

    public Guild update(Long id, Guild input) {
        Guild guild = findById(id);
        guild.setOwner(input.getOwner()); guild.setName(input.getName()); guild.setDescription(input.getDescription());
        return guilds.save(guild);
    }
    
    public void delete(Long id) { guilds.delete(findById(id)); }
}

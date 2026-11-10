package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.GuildEntity;
import com.backand.tambayanguild.repository.GuildRepository;
import org.springframework.stereotype.Service;
import java.util.List;

//Implements CRUD operations for GuildEntity entity
@Service
public class GuildService {

    private final GuildRepository guilds;

    public GuildService(GuildRepository guilds) { 
        this.guilds = guilds; 
    }

    public List<GuildEntity> findAll() { 
        return guilds.findAll(); 
    }

    public GuildEntity findById(Long id) { 
        return guilds.findById(id).orElseThrow(() -> new IllegalArgumentException("GuildEntity not found: " + id)); 
    }

    public GuildEntity create(GuildEntity GuildEntity) { 
        GuildEntity.setId(null); return guilds.save(GuildEntity); 
    }

    public GuildEntity update(Long id, GuildEntity input) {
        GuildEntity GuildEntity = findById(id);
        GuildEntity.setOwner(input.getOwner()); GuildEntity.setName(input.getName()); GuildEntity.setDescription(input.getDescription());
        return guilds.save(GuildEntity);
    }
    
    public void delete(Long id) { guilds.delete(findById(id)); }
}

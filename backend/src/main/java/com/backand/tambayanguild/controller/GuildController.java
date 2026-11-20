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

    public GuildController(GuildService service) { 
        this.service = service; 
    }

    @GetMapping 
    public List<GuildEntity> getAll() {
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public GuildEntity getOne(@PathVariable Long id) { 
        return service.findById(id); 
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED) 
    public GuildEntity create(@RequestBody GuildEntity GuildEntity) { 
        return service.create(GuildEntity); 
    }

    @PutMapping("/{id}") 
    public GuildEntity update(@PathVariable Long id, @RequestBody GuildEntity GuildEntity) { 
            return service.update(id, GuildEntity); 
    }

    @DeleteMapping("/{id}") 
    
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) {
        service.delete(id); 
        }
}

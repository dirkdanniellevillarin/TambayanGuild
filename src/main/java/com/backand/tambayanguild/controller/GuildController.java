package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.Guild;
import com.backand.tambayanguild.service.GuildService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//REST endpoints setters and getters for guild entity
@RestController
@RequestMapping("/api/guilds")

public class GuildController {

    private final GuildService service;

    public GuildController(GuildService service) { 
        this.service = service; 
    }

    @GetMapping 
    public List<Guild> getAll() {
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public Guild getOne(@PathVariable Long id) { 
        return service.findById(id); 
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED) 
    public Guild create(@RequestBody Guild guild) { 
        return service.create(guild); 
    }

    @PutMapping("/{id}") 
    public Guild update(@PathVariable Long id, @RequestBody Guild guild) { 
        return service.update(id, guild); 
    }

    @DeleteMapping("/{id}") 
    
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) {
        service.delete(id); 
        }
}

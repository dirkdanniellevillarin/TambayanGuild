package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.DeckEntity;
import com.backand.tambayanguild.service.DeckService;

@RestController
@RequestMapping("/api/decks")
public class DeckController {
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;
    @Autowired
    DeckService dserv;

    public DeckController() {
    }

    // Create
    @PostMapping("/postNewDeck")
    public Object postDeck(@RequestBody DeckEntity deck) {
        current.requireUser(deck.getCreatedBy()==null?null:deck.getCreatedBy().getId());
        deck.setCreatedBy(new com.backand.tambayanguild.entity.UserEntity());deck.getCreatedBy().setId(current.requireUserId());
        return dserv.deckView(dserv.postDeck(deck));
    }

    // Read
    @GetMapping("/getAllDecks")
    public List<Object> getAllDecks() {
        current.requireUser(null);return dserv.getAllDecksForUser(current.requireUserId()).stream().map(d->(Object)dserv.deckView(d)).toList();
    }

    @GetMapping("/getDecksByGuild/{guildId}")
    public List<Object> getDecksByGuild(@PathVariable Long guildId) {
        current.requireUser(null);return dserv.getDecksForMember(guildId,current.requireUserId()).stream().map(d->(Object)dserv.deckView(d)).toList();
    }

    // Update
    @PutMapping("/putDeckDetails")
    public Object putDeckDetails(@RequestParam Long id,@RequestParam(required=false) Long actorId, @RequestBody DeckEntity newDeckDetails) {
        current.requireUser(actorId);
        return dserv.deckView(dserv.putDeckDetails(id, newDeckDetails));
    }

    // Delete
    @DeleteMapping("/deleteDeck/{id}")
    public String deleteDeck(@PathVariable Long id,@RequestParam(required=false) Long actorId) {
        current.requireUser(actorId);
        return dserv.deleteDeck(id);
    }
}

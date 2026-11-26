package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.service.RewardService;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/rewards") public class RewardController {
 private final RewardService rewards;public RewardController(RewardService rewards){this.rewards=rewards;}
 @org.springframework.beans.factory.annotation.Autowired com.backand.tambayanguild.service.CurrentUserService current;
 @GetMapping("/store") public Object catalog(){current.requireUserId();return rewards.catalogViews();}
 @GetMapping("/users/{userId}") public Object inventory(@PathVariable Long userId){current.requireUser(userId);return rewards.inventoryViews(current.requireUserId());}
 @PostMapping("/users/{userId}/purchase/{code}") public Object purchase(@PathVariable Long userId,@PathVariable String code){current.requireUser(userId);return rewards.purchase(current.requireUserId(),code);}
}

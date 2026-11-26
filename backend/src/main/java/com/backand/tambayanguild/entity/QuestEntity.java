package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "quests", uniqueConstraints = @UniqueConstraint(columnNames = {"quest_date", "code"}))
public class QuestEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 32) private String code;
    @Column(nullable = false) private String name;
    @Column(nullable = false, columnDefinition = "TEXT") private String description;
    @Column(name = "target_count", nullable = false) private int targetCount;
    @Column(name = "reward_xp", nullable = false) private int rewardXp;
    @Column(name = "reward_coins", nullable = false) private int rewardCoins;
    @Column(name = "quest_date", nullable = false) private LocalDate questDate;
    protected QuestEntity() {}
    public QuestEntity(String code, String name, String description, int targetCount, int rewardXp, int rewardCoins, LocalDate questDate) {
        this.code=code; this.name=name; this.description=description; this.targetCount=targetCount;
        this.rewardXp=rewardXp; this.rewardCoins=rewardCoins; this.questDate=questDate;
    }
    public Long getId(){return id;} public String getCode(){return code;} public String getName(){return name;}
    public String getDescription(){return description;} public int getTargetCount(){return targetCount;}
    public int getRewardXp(){return rewardXp;} public int getRewardCoins(){return rewardCoins;} public LocalDate getQuestDate(){return questDate;}
}

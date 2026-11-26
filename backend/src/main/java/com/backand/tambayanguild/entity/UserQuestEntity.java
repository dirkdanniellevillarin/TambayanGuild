package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_quests", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "quest_id"}))
public class UserQuestEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="user_id", nullable=false) private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="quest_id", nullable=false) private QuestEntity quest;
    @Column(name="progress_count", nullable=false) private int progressCount;
    @Column(nullable=false) private boolean completed;
    @Column(name="completed_at") private LocalDateTime completedAt;
    protected UserQuestEntity() {}
    public UserQuestEntity(UserEntity user, QuestEntity quest) { this.user=user; this.quest=quest; }
    public Long getId(){return id;} public UserEntity getUser(){return user;} public QuestEntity getQuest(){return quest;}
    public int getProgressCount(){return progressCount;} public void setProgressCount(int count){this.progressCount=count;}
    public boolean isCompleted(){return completed;} public void complete(){this.completed=true;this.completedAt=LocalDateTime.now();}
    public LocalDateTime getCompletedAt(){return completedAt;}
}

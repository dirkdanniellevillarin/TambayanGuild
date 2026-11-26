package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="chat_messages")
public class ChatMessageEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="guild_id",nullable=false) private GuildEntity guild;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @Column(nullable=false,length=2000) private String content;
    @Column(name="sent_at",nullable=false) private LocalDateTime sentAt;
    protected ChatMessageEntity() {}
    public ChatMessageEntity(GuildEntity guild,UserEntity user,String content){this.guild=guild;this.user=user;this.content=content;this.sentAt=LocalDateTime.now();}
    public Long getId(){return id;} public Long getGuildId(){return guild.getId();} public Long getUserId(){return user.getId();} public String getUsername(){return user.getUsername();} public String getContent(){return content;} public LocalDateTime getSentAt(){return sentAt;}
}

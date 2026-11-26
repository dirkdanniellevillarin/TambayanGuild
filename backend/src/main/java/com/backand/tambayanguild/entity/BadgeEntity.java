package com.backand.tambayanguild.entity;

import jakarta.persistence.*;

@Entity @Table(name="badges", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","code"}))
public class BadgeEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @Column(nullable=false,length=40) private String code;
    @Column(nullable=false) private String name;
    @Column(nullable=false,columnDefinition="TEXT") private String description;
    @Column(name="earned_at",nullable=false) private java.time.LocalDateTime earnedAt;
    protected BadgeEntity() {}
    public BadgeEntity(UserEntity user,String code,String name,String description){this.user=user;this.code=code;this.name=name;this.description=description;this.earnedAt=java.time.LocalDateTime.now();}
    public Long getId(){return id;} public Long getUserId(){return user.getId();} public String getCode(){return code;} public String getName(){return name;} public String getDescription(){return description;} public java.time.LocalDateTime getEarnedAt(){return earnedAt;}
}

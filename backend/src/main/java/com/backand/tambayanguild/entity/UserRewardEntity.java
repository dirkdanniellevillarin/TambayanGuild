package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="user_rewards",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","reward_item_id"}))
public class UserRewardEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id",nullable=false) private UserEntity user;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="reward_item_id",nullable=false) private RewardItemEntity item;
    @Column(name="purchased_at",nullable=false) private LocalDateTime purchasedAt;
    protected UserRewardEntity() {}
    public UserRewardEntity(UserEntity user,RewardItemEntity item){this.user=user;this.item=item;this.purchasedAt=LocalDateTime.now();}
    public Long getId(){return id;} public RewardItemEntity getItem(){return item;} public LocalDateTime getPurchasedAt(){return purchasedAt;}
}

package com.backand.tambayanguild.entity;

import jakarta.persistence.*;

@Entity @Table(name="reward_items")
public class RewardItemEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=40) private String code;
    @Column(nullable=false) private String name;
    @Column(nullable=false,columnDefinition="TEXT") private String description;
    @Column(nullable=false) private long price;
    @Column(nullable=false) private String category;
    @Column(nullable=false) private boolean active=true;
    protected RewardItemEntity() {}
    public RewardItemEntity(String code,String name,String description,long price,String category){this.code=code;this.name=name;this.description=description;this.price=price;this.category=category;}
    public Long getId(){return id;} public String getCode(){return code;} public String getName(){return name;} public String getDescription(){return description;} public long getPrice(){return price;} public String getCategory(){return category;} public boolean isActive(){return active;}
}

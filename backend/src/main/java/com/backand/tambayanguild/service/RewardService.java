package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.*;
import com.backand.tambayanguild.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service public class RewardService {
 private final RewardItemRepository items;private final UserRewardRepository owned;private final UserRepository users;private final ProgressService progress;
 public RewardService(RewardItemRepository items,UserRewardRepository owned,UserRepository users,ProgressService progress){this.items=items;this.owned=owned;this.users=users;this.progress=progress;}
 public List<RewardItemEntity> catalog(){return items.findByActiveTrueOrderByPriceAsc();}
 public List<UserRewardEntity> inventory(Long userId){progress.user(userId);return owned.findByUser_IdOrderByPurchasedAtDesc(userId);}
 @Transactional public java.util.Map<String,Object> purchase(Long userId,String code){UserEntity user=progress.user(userId);RewardItemEntity item=items.findByCode(code).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Reward not found"));if(!item.isActive())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Reward not available");if(owned.existsByUser_IdAndItem_Id(userId,item.getId()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Reward already owned");if(user.getCoins()<item.getPrice())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough coins");user.setCoins(user.getCoins()-item.getPrice());owned.save(new UserRewardEntity(user,item));return userRewardView(item);}
 public java.util.Map<String,Object> userRewardView(RewardItemEntity item){var out=new java.util.LinkedHashMap<String,Object>();out.put("code",item.getCode());out.put("name",item.getName());out.put("description",item.getDescription());out.put("price",item.getPrice());out.put("category",item.getCategory());return out;}
 public List<java.util.Map<String,Object>> catalogViews(){return catalog().stream().map(this::userRewardView).toList();}
 public List<java.util.Map<String,Object>> inventoryViews(Long userId){return inventory(userId).stream().map(r->userRewardView(r.getItem())).toList();}
 @Transactional public void seed(){if(items.count()>0)return;items.save(new RewardItemEntity("avatar-scholar","Scholar Avatar","A scholarly avatar frame",100,"AVATAR"));items.save(new RewardItemEntity("avatar-starlight","Starlight Avatar","A luminous avatar frame",250,"AVATAR"));items.save(new RewardItemEntity("theme-violet","Violet Profile Theme","A violet profile theme",150,"THEME"));}
}

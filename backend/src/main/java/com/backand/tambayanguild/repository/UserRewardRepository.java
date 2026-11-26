package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.UserRewardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface UserRewardRepository extends JpaRepository<UserRewardEntity,Long> {
 List<UserRewardEntity> findByUser_IdOrderByPurchasedAtDesc(Long userId);
 boolean existsByUser_IdAndItem_Id(Long userId,Long itemId);
}

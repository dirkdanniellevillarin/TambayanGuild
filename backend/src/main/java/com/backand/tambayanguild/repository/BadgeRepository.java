package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BadgeRepository extends JpaRepository<BadgeEntity,Long> {
 List<BadgeEntity> findByUser_IdOrderByEarnedAtDesc(Long userId);
 boolean existsByUser_IdAndCode(Long userId,String code);
}

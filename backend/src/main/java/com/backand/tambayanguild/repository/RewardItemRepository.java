package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.RewardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface RewardItemRepository extends JpaRepository<RewardItemEntity,Long> {
 List<RewardItemEntity> findByActiveTrueOrderByPriceAsc();
 Optional<RewardItemEntity> findByCode(String code);
}

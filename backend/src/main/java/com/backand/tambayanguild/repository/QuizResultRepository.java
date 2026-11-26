package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface QuizResultRepository extends JpaRepository<QuizResultEntity,Long> {
 Optional<QuizResultEntity> findByGroupQuiz_IdAndUser_Id(Long quizId,Long userId);
 List<QuizResultEntity> findByGroupQuiz_IdOrderByScoreDesc(Long quizId);
}

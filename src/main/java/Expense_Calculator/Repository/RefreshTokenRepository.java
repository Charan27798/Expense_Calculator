package Expense_Calculator.Repository;

import Expense_Calculator.Entity.RefreshTokenEntity;
import Expense_Calculator.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUser(UserEntity user);

    Optional<RefreshTokenEntity> findByUser(UserEntity user);
}
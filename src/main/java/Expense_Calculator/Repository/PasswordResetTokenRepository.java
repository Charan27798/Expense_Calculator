
package Expense_Calculator.Repository;

import Expense_Calculator.Entity.PasswordResetTokenEntity;
import Expense_Calculator.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

    Optional<PasswordResetTokenEntity> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);
}
package Expense_Calculator.Repository;

import Expense_Calculator.Entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity,Long> {
    List<ExpenseEntity> findByTitleContaining(String title);

    List<ExpenseEntity> findByAmountBetween(Double minAmount, Double maxAmount);
}

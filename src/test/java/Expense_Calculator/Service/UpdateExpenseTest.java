package Expense_Calculator.Service;
import Expense_Calculator.Entity.CategoryEntity;
import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Repository.CategoryRepository;
import Expense_Calculator.Repository.UserRepository;
import Expense_Calculator.RequestDTO.ExpenseRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import Expense_Calculator.ResponseDTO.ExpenseResponseDTO;
import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.Repository.ExpenseRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
public class UpdateExpenseTest {

    @Mock
    private ExpenseRepository expenseRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void shouldUpdateExpense() {

        // Create dummy User because mapToResponseDTO()
        // needs expense.getUser().getName()
        UserEntity user = new UserEntity();
        user.setName("Charan");

        // Create dummy Category because mapToResponseDTO()
        // needs expense.getCategory().getName()
        CategoryEntity category = new CategoryEntity();
        category.setName("Food");

        // Existing expense from the repository
        ExpenseEntity expense = new ExpenseEntity();
        expense.setId(2L);
        expense.setTitle("Old Lunch");
        expense.setDescription("Old description");
        expense.setAmount(300.0);
        expense.setUser(user);
        expense.setCategory(category);

        // New values coming from the request
        ExpenseRequestDTO dto = new ExpenseRequestDTO();
        dto.setTitle("Updated Lunch");
        dto.setDescription("Updated description");
        dto.setAmount(500.0);
        dto.setExpenseDate(LocalDateTime.now());

        // Mockito:
        // Pretend expense ID 2 exists in the database
        when(expenseRepo.findById(2L))
                .thenReturn(Optional.of(expense));

        // Mockito:
        // Pretend save() successfully saves and returns the updated expense
        when(expenseRepo.save(any(ExpenseEntity.class)))
                .thenReturn(expense);

        // Call the REAL service method
        ExpenseResponseDTO response =
                expenseService.updateExpense(2L, dto);

        // JUnit:
        // Check that the updated values are returned
        assertEquals(2L, response.getId());
        assertEquals("Updated Lunch", response.getTitle());
        assertEquals("Updated description", response.getDescription());
        assertEquals(500.0, response.getAmount());
        assertEquals("Charan", response.getUserName());
        assertEquals("Food", response.getCategoryName());

        // Mockito:
        // Verify that the service searched for the expense
        verify(expenseRepo).findById(2L);

        // Mockito:
        // Verify that the updated expense was saved
        verify(expenseRepo).save(any(ExpenseEntity.class));
    }
}

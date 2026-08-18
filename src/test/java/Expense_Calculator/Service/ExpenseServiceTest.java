package Expense_Calculator.Service;

import Expense_Calculator.Entity.CategoryEntity;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Exception.ResourceNotFoundException;
import Expense_Calculator.Repository.CategoryRepository;
import Expense_Calculator.Repository.ExpenseRepository;
import Expense_Calculator.Repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.ResponseDTO.ExpenseResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import Expense_Calculator.RequestDTO.ExpenseRequestDTO;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private ExpenseService expenseService;


    @Test
    void shouldGetExpenseById() {

        UserEntity user = new UserEntity();
        user.setName("Charan");

        CategoryEntity category = new CategoryEntity();
        category.setName("Food");

        // Dummy data
        ExpenseEntity expense = new ExpenseEntity();
        expense.setId(2L);
        expense.setTitle("Lunch");
        expense.setDescription("Office lunch");
        expense.setAmount(500.0);

        expense.setUser(user);
        expense.setCategory(category);

        // Tell the mock repository what to return
        when(expenseRepo.findById(2L))
                .thenReturn(Optional.of(expense));

        // Call the REAL service method
        ExpenseResponseDTO response =
                expenseService.getExpense(2L);

        // Verify the result
        assertEquals(2L, response.getId());
        assertEquals("Lunch", response.getTitle());
        assertEquals("Office lunch", response.getDescription());
        assertEquals(500.0, response.getAmount());
        assertEquals("Charan", response.getUserName());
        assertEquals("Food", response.getCategoryName());

        verify(expenseRepo).findById(2L);
    }

    @Test
    void shouldThrowExceptionWhenExpenseNotFound() {

        // Tell Mockito: expense ID 99 does not exist
        when(expenseRepo.findById(99L))
                .thenReturn(Optional.empty());

        // Call the real service and expect an exception
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> expenseService.getExpense(99L)
                );

        // Check exception message
        assertEquals(
                "Expense not found",
                exception.getMessage()
        );

        // Verify repository was called
        verify(expenseRepo).findById(99L);
    }

    @Test
    void shouldhrowExceptionWhenExpenseNotFound() {

        when(expenseRepo.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> expenseService.getExpense(99L)
                );

        assertEquals(
                "Expense not found",
                exception.getMessage()
        );

        verify(expenseRepo).findById(99L);
    }

    @Test
    void shouldDeleteExpense() {

        ExpenseEntity expense = new ExpenseEntity();
        expense.setId(2L);

        // Pretend expense 2 exists
        when(expenseRepo.findById(2L))
                .thenReturn(Optional.of(expense));

        // Call REAL service method
        expenseService.deleteExpense(2L);

        // Verify repository interactions
        verify(expenseRepo).findById(2L);
        verify(expenseRepo).deleteById(2L);
    }

    @Test
    void shouldNotDeleteWhenExpenseNotFound() {

        // Pretend expense 99 doesn't exist
        when(expenseRepo.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> expenseService.deleteExpense(99L)
        );

        // findById should have been called
        verify(expenseRepo).findById(99L);

        // deleteById should NEVER be called
        verify(expenseRepo, never())
                .deleteById(99L);
    }

    @Test
    void shouldCreateExpense() {

        // Create dummy User
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Charan");

        // Create dummy Category
        CategoryEntity category = new CategoryEntity();
        category.setId(10L);
        category.setName("Food");

        // Create request DTO - this is the input to our service
        ExpenseRequestDTO request = new ExpenseRequestDTO();
        request.setTitle("Lunch");
        request.setDescription("Office lunch");
        request.setAmount(500.0);
        request.setUserId(1L);
        request.setCategoryId(10L);

        // Mockito: when service asks for user ID 1,
        // return our dummy user
        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        // Mockito: when service asks for category ID 10,
        // return our dummy category
        when(categoryRepo.findById(10L))
                .thenReturn(Optional.of(category));

        // Create the expense that we expect the repository
        // to return after save()
        ExpenseEntity savedExpense = new ExpenseEntity();
        savedExpense.setId(2L);
        savedExpense.setTitle("Lunch");
        savedExpense.setDescription("Office lunch");
        savedExpense.setAmount(500.0);
        savedExpense.setUser(user);
        savedExpense.setCategory(category);

        // Mockito: when expenseRepo.save() is called,
        // return our dummy saved expense
        when(expenseRepo.save(any(ExpenseEntity.class)))
                .thenReturn(savedExpense);

        // Call the REAL service method
        ExpenseResponseDTO response =
                expenseService.createExpense(request);

        // JUnit: check the response
        assertEquals(2L, response.getId());
        assertEquals("Lunch", response.getTitle());
        assertEquals("Office lunch", response.getDescription());
        assertEquals(500.0, response.getAmount());
        assertEquals("Charan", response.getUserName());
        assertEquals("Food", response.getCategoryName());

        // Mockito: verify the repositories were called
        verify(userRepo).findById(1L);
        verify(categoryRepo).findById(10L);
        verify(expenseRepo).save(any(ExpenseEntity.class));
    }
}
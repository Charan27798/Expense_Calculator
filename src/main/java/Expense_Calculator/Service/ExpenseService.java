package Expense_Calculator.Service;

import Expense_Calculator.RequestDTO.ExpenseRequestDTO;
import Expense_Calculator.Entity.CategoryEntity;
import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.Entity.UserEntity;
import Expense_Calculator.Exception.ResourceNotFoundException;
import Expense_Calculator.Repository.CategoryRepository;
import Expense_Calculator.Repository.ExpenseRepository;
import Expense_Calculator.Repository.UserRepository;
import Expense_Calculator.ResponseDTO.ExpenseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

//    @Autowired
//    private UserRepository userRepo;
//    @Autowired
//    private ExpenseRepository expenseRepo;
//    @Autowired
//    private CategoryRepository categoryRepo;

    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ExpenseRepository expenseRepo;


    public ExpenseService(UserRepository userRepo,
                          CategoryRepository categoryRepo,
                          ExpenseRepository expenseRepo){
        this.userRepo = userRepo;
        this.categoryRepo=categoryRepo;
        this.expenseRepo=expenseRepo;
    }


    public ExpenseResponseDTO createExpense(ExpenseRequestDTO expenseDTO){

        UserEntity user = userRepo
                .findById(expenseDTO.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        CategoryEntity category = categoryRepo
                .findById(expenseDTO.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category not found"));

        ExpenseEntity expense = new ExpenseEntity();

        expense.setTitle(expenseDTO.getTitle());
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setExpenseDate(expenseDTO.getExpenseDate());
        expense.setUser(user);
        expense.setCategory(category);

        ExpenseEntity savedExpense =
                expenseRepo.save(expense);

        return mapToResponseDTO(savedExpense);

    }


    public List<ExpenseResponseDTO> getAllExpenses() {
        List<ExpenseEntity> expenses = expenseRepo.findAll();

        List<ExpenseResponseDTO> responses = new ArrayList<>();
        for(ExpenseEntity expense1: expenses){
            responses.add(
                    mapToResponseDTO(expense1)
            );
        }
        return responses;
    }

//    public Page<ExpenseResponseDTO> getAllExpenses(Pageable pageable) {
//        Page<ExpenseEntity> expenses = expenseRepo.findAll(pageable);

//        List<ExpenseResponseDTO> responses = new ArrayList<>();
//        for(ExpenseEntity expense1: expenses){
//            responses.add(
//                    mapToResponseDTO(expense1)
//            );
//        }
//        return expenses.map(this::mapToResponseDTO);
//    }

    public ExpenseResponseDTO getExpense(Long id){

        ExpenseEntity expense = expenseRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        return mapToResponseDTO(expense);
    }

    private ExpenseResponseDTO mapToResponseDTO(
            ExpenseEntity expense){

        ExpenseResponseDTO response = new ExpenseResponseDTO();

        response.setId(expense.getId());

        response.setTitle(expense.getTitle());

        response.setDescription(expense.getDescription());

        response.setAmount(expense.getAmount());

        response.setExpenseDate(expense.getExpenseDate());

        response.setUserName(
                expense.getUser().getName()
        );

        response.setCategoryName(
                expense.getCategory().getName()
        );

        return response;

    }

    public void deleteExpense(long id) {
         expenseRepo.findById(id)
                 .orElseThrow(()->new ResourceNotFoundException("Expense not found"));

         expenseRepo.deleteById(id);
    }

    public ExpenseResponseDTO updateExpense(long id, ExpenseRequestDTO dto) {
        ExpenseEntity expense = expenseRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Expense not found"));

        expense.setTitle(dto.getTitle());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());

        ExpenseEntity saved = expenseRepo.save(expense);
          return mapToResponseDTO(saved);
    }

    public List<ExpenseResponseDTO> searchByTitle(String title) {
        List<ExpenseEntity> expenses = expenseRepo.findByTitleContaining(title);

        List<ExpenseResponseDTO> response = new ArrayList<>();

        for (ExpenseEntity expense:expenses){
            response.add(mapToResponseDTO(expense));
        }

        return response;
    }

    public List<ExpenseResponseDTO> filterByAmount(Double minAmount, Double maxAmount) {
        List<ExpenseEntity> expenses =
                expenseRepo.findByAmountBetween(minAmount, maxAmount);

        List<ExpenseResponseDTO> response = new ArrayList<>();

        for(ExpenseEntity expense:expenses){
            response.add(mapToResponseDTO(expense));
        }

        return response;
    }
}

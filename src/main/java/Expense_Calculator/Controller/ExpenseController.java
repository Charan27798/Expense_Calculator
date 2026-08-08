package Expense_Calculator.Controller;
import Expense_Calculator.RequestDTO.ExpenseRequestDTO;
import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.ResponseDTO.ExpenseResponseDTO;
import Expense_Calculator.Service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService=expenseService;
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {

        return "Welcome Admin!";

    }

    @PostMapping
    public ExpenseResponseDTO createExpense(@Valid @RequestBody ExpenseRequestDTO dto){
        return expenseService.createExpense(dto);
    }

    @GetMapping("/allExpenses")
    public List<ExpenseResponseDTO> getAllExpences(){
        return expenseService.getAllExpenses();
    }
//      @GetMapping("/allExpenses")
//      public Page<ExpenseResponseDTO> getAllExpenses(@RequestParam int page,
//                                                     @RequestParam int size,
//                                                     @RequestParam String SortBy,
//                                                     @RequestParam String direction) {
//
//          Sort sort = Sort.by(
//                  Sort.Direction.fromString(direction),
//                  SortBy
//          );
//
//          Pageable pageable = PageRequest.of(page,size,sort);
//          return expenseService.getAllExpenses(pageable);
//      }


    @GetMapping("/getExpense/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ExpenseResponseDTO getExpense(@PathVariable long id){
        return expenseService.getExpense(id);
    }

    @DeleteMapping("/deleteExpense/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExpense(@PathVariable long id){
        expenseService.deleteExpense(id);
    }

    @PutMapping("/updateExpense/{id}")
    public ExpenseResponseDTO updateExpense(@PathVariable long id,@Valid @RequestBody ExpenseRequestDTO dto){
        return expenseService.updateExpense(id,dto);
    }

    @GetMapping("/search")
    public List<ExpenseResponseDTO> searchExpense(@RequestParam String title){
        return expenseService.searchByTitle(title);
    }

    @GetMapping("/searchByAmount")
    public List<ExpenseResponseDTO> searchByAmount(
            @RequestParam Double minAmount,
            @RequestParam Double maxAmount) {

        return expenseService.filterByAmount(
                minAmount,
                maxAmount);
    }
}

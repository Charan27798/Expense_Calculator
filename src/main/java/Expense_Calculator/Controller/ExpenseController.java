package Expense_Calculator.Controller;
import Expense_Calculator.RequestDTO.ExpenseRequestDTO;
import Expense_Calculator.Entity.ExpenseEntity;
import Expense_Calculator.ResponseDTO.ExpenseResponseDTO;
import Expense_Calculator.Service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping("addExpense")
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

    @Operation(
            summary = "Get expense by ID",
            description = "Fetches an expense using its unique ID"
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Expense retrieved successfully"
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - valid JWT token required"
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Expense not found"
            )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/getExpense/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ExpenseResponseDTO getExpense(@PathVariable long id){
        return expenseService.getExpense(id);
    }

    @DeleteMapping("/deleteExpense/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExpense(
            @Parameter(
            description = "ID of the expense",
            example = "4"
    )@PathVariable long id){
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

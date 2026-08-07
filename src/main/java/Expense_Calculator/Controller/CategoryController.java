package Expense_Calculator.Controller;


import Expense_Calculator.RequestDTO.CategoryRequestDTO;
import Expense_Calculator.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @PostMapping
    public void createCategory(@Valid @RequestBody CategoryRequestDTO dto){
        categoryService.createCategory(dto);
    }
}

package Expense_Calculator.Service;


import Expense_Calculator.RequestDTO.CategoryRequestDTO;
import Expense_Calculator.Entity.CategoryEntity;
import Expense_Calculator.Repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    public void createCategory(CategoryRequestDTO dto){

        CategoryEntity category = new CategoryEntity();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
    }
}

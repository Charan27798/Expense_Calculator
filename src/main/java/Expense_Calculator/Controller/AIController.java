package Expense_Calculator.Controller;

import Expense_Calculator.RequestDTO.AIChatRequestDTO;
import Expense_Calculator.Service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody AIChatRequestDTO request) {

        return aiService.chat(request.getMessage());
    }
}
package Expense_Calculator.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AIChatRequestDTO {

    @NotBlank(message = "Message is required")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
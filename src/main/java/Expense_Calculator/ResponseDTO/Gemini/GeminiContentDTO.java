package Expense_Calculator.ResponseDTO.Gemini;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiContentDTO {

    private List<GeminiPartDTO> parts;
    private String role;


    public List<GeminiPartDTO> getParts() {
        return parts;
    }

    public void setParts(List<GeminiPartDTO> parts) {
        this.parts = parts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

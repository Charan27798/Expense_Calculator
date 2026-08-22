package Expense_Calculator.ResponseDTO.Gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class GeminiCandidateDTO {

    private GeminiContentDTO content;

    public GeminiContentDTO getContent() {
        return content;
    }

    public void setContent(GeminiContentDTO content) {
        this.content = content;
    }
}

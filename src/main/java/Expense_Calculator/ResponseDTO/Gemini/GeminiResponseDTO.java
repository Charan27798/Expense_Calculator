package Expense_Calculator.ResponseDTO.Gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class GeminiResponseDTO {

    private List<GeminiCandidateDTO> candidates;

    public List<GeminiCandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<GeminiCandidateDTO> candidates) {
        this.candidates = candidates;
    }
}

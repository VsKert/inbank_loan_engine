package proov.inbank.loan_engine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotNull(message = "Loan period is required.")
    @Min(value = 12, message = "Minimum loan period is 12 months.")
    @Max(value = 60, message = "Maximum loan period is 60 months.")
    private int loanPeriod;

    @NotNull(message = "Loan amount is required.")
    @Min(value = 2000, message = "Minimum allowed loan amount is 2000.")
    @Max(value = 10000, message = "Maximum allowed loan amount is 10000.")
    private BigDecimal amount;
}

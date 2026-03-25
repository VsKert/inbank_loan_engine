package proov.inbank.loan_engine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanResponse {
    @NotNull(message = "Loan amount is required.")
    @Min(value = 2000, message = "Minimum allowed loan amount is 2000.")
    @Max(value = 10000, message = "Maximum allowed loan amount is 10000.")
    private BigDecimal amount;

    @NotNull(message = "Loan decision is required.")
    private boolean approved;

    // Null by default, only used for increasing period to find suitable loan.
    private Integer updatedMinimumLoanPeriod;
}

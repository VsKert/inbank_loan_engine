package proov.inbank.loan_engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import proov.inbank.loan_engine.dto.LoanResponse;

import java.math.BigDecimal;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DebtException.class)
    public ResponseEntity<LoanResponse> handleDebtException(DebtException ex) {
        LoanResponse response = LoanResponse.builder().approved(false).amount(BigDecimal.ZERO).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}

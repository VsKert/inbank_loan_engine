package proov.inbank.loan_engine.exception;

import lombok.Getter;

@Getter
public class DebtException extends RuntimeException {
    public DebtException() {
        super("User is ineligible for loans due to debt.");
    }
}

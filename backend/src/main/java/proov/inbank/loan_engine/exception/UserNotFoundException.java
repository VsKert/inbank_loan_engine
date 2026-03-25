package proov.inbank.loan_engine.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id: " + id.toString() + " not found.");
    }
}

package proov.inbank.loan_engine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proov.inbank.loan_engine.dto.LoanRequest;
import proov.inbank.loan_engine.dto.LoanResponse;
import proov.inbank.loan_engine.service.MockLoanService;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {
    private final MockLoanService loanService;

    @GetMapping("/health")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Alive");
    }

    @GetMapping("/apply")
    public ResponseEntity<LoanResponse> requestLoan(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.requestLoan(request));
    }

}

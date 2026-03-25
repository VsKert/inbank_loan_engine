package proov.inbank.loan_engine.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proov.inbank.loan_engine.dto.LoanRequest;
import proov.inbank.loan_engine.dto.LoanResponse;
import proov.inbank.loan_engine.service.MockLoanService;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
@Slf4j
public class LoanController {
    private final MockLoanService loanService;

    @GetMapping("/health")
    public ResponseEntity<String> testEndpoint() {
        log.info("GET /api/loan/health - Health check");
        return ResponseEntity.ok("Alive");
    }

    @PostMapping("/apply")
    public ResponseEntity<LoanResponse> requestLoan(@RequestBody LoanRequest request) {
        log.info("POST /api/loan/apply - userId={}, loanPeriod={}, amount={}",
                request.getUserId(), request.getLoanPeriod(), request.getAmount());
        return ResponseEntity.ok(loanService.requestLoan(request));
    }
}

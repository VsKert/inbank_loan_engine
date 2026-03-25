package proov.inbank.loan_engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proov.inbank.loan_engine.dto.LoanRequest;
import proov.inbank.loan_engine.dto.LoanResponse;
import proov.inbank.loan_engine.entity.CreditSegment;
import proov.inbank.loan_engine.entity.MockLoan;
import proov.inbank.loan_engine.repository.MockLoanRepository;
import proov.inbank.loan_engine.service.MockLoanService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanEngineTests {
    @Mock
    private MockLoanRepository mockLoanRepository;

    @InjectMocks
    private MockLoanService loanService;

    @Test
    void shouldReturnApprovedLoan() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_2);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(2000))
                .loanPeriod(12).build();

        LoanResponse response = loanService.requestLoan(request);

        assertTrue(response.isApproved());
        assertEquals(BigDecimal.valueOf(3600), response.getAmount());
    }

    @Test
    void noAllowedLoanOnDebt() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.DEBT);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(4000))
                .loanPeriod(12).build();

        LoanResponse response = loanService.requestLoan(request);

        assertFalse(response.isApproved());
        assertEquals(BigDecimal.valueOf(0), response.getAmount());
    }

    @Test
    void noSuitableLoanAmountInPeriodFindsMaximumAmountClosestAllowedPeriod() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_1);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));


        // Following loan is not allowed, as score is under one
        // Engine will attempt to recommend maximum amount in that period
        // Recommendation is 1200, which is under minimum amount of 2000.
        // Engine will try to find minimum allowed loan by expanding loan period
        // Return value is the maximum allowed amount in that minimum period
        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(2500))
                .loanPeriod(12).build();

        LoanResponse response = loanService.requestLoan(request);

        assertFalse(response.isApproved());
        assertEquals(20, response.getUpdatedMinimumLoanPeriod());
        assertEquals(BigDecimal.valueOf(2000), response.getAmount());
    }

    @Test
    void maximumAllowedLoanOver10000GetsCapped() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_3);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        // Uncapped maximum allowed loan is 50'000
        // Should be capped to 10'000
        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(5000))
                .loanPeriod(50).build();

        LoanResponse response = loanService.requestLoan(request);

        assertTrue(response.isApproved());
        assertEquals(BigDecimal.valueOf(10000), response.getAmount());
    }

    @Test
    void testRequestedLoanAmountOver10000NotApproved() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_3);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        // Uncapped maximum allowed loan is 50'000
        // Should be capped to 10'000
        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(20000))
                .loanPeriod(50).build();

        LoanResponse response = loanService.requestLoan(request);

        assertFalse(response.isApproved());
        assertEquals(BigDecimal.valueOf(10000), response.getAmount());
    }

    @Test
    void testRequestedLoanAmountUnder2000NotApproved() {
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_2);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        // Uncapped maximum allowed loan is 50'000
        // Should be capped to 10'000
        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(1200))
                .loanPeriod(24).build();

        LoanResponse response = loanService.requestLoan(request);

        assertFalse(response.isApproved());
        assertEquals(BigDecimal.valueOf(7200), response.getAmount());
    }

    @Test
    void unsuitableLoanNotApproved() { // Test for when requested loan is higher than allowed, but has valid maximum in that period
        MockLoan loan = new MockLoan();
        loan.setSegment(CreditSegment.SEGMENT_2);

        when(mockLoanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        LoanRequest request = LoanRequest.builder()
                .userId(1L)
                .amount(BigDecimal.valueOf(8000))
                .loanPeriod(24).build();

        LoanResponse response = loanService.requestLoan(request);

        assertFalse(response.isApproved());
        assertEquals(BigDecimal.valueOf(7200), response.getAmount());
    }
}

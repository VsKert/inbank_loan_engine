package proov.inbank.loan_engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import proov.inbank.loan_engine.dto.LoanRequest;
import proov.inbank.loan_engine.dto.LoanResponse;
import proov.inbank.loan_engine.entity.CreditSegment;
import proov.inbank.loan_engine.entity.MockLoan;
import proov.inbank.loan_engine.exception.DebtException;
import proov.inbank.loan_engine.exception.NoAllowedLoanException;
import proov.inbank.loan_engine.exception.UserNotFoundException;
import proov.inbank.loan_engine.repository.MockLoanRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class MockLoanService {
    private final MockLoanRepository mockLoanRepository;
    private static final int MAXIMUM_ALLOWED_LOAN_PERIOD = 60;
    private static final BigDecimal MINIMUM_APPROVED_CREDIT_SCORE = BigDecimal.ONE;
    private static final BigDecimal MAXIMUM_ALLOWED_LOAN_AMOUNT = BigDecimal.valueOf(10000);
    private static final BigDecimal MINIMUM_ALLOWED_LOAN_AMOUNT = BigDecimal.valueOf(2000);


    public LoanResponse requestLoan(LoanRequest request) {
        Long userId = request.getUserId();
        MockLoan userData = mockLoanRepository.findById(userId)
                .orElseThrow(() ->  {
                    log.info("User with id={} not found.", userId);
                    return new UserNotFoundException(userId);
                });

        BigDecimal loanAmount = request.getAmount();
        CreditSegment userSegment = userData.getSegment();

        if (userSegment.compare(CreditSegment.DEBT) == 0) {
            log.info("Log request for userId={} rejected: user is in debt.", userId);
            throw new DebtException();
        }

        int loanPeriod = request.getLoanPeriod();
        int userCreditModifier = userSegment.getCreditModifier();
        BigDecimal creditScore =  scoringAlgorithm(
                userCreditModifier,
                loanPeriod,
                loanAmount);

        boolean loanRangeApproved = loanAmount.compareTo(MAXIMUM_ALLOWED_LOAN_AMOUNT) <= 0 &&
                loanAmount.compareTo(MINIMUM_ALLOWED_LOAN_AMOUNT) >= 0;
        boolean creditApproved = creditScore.compareTo(MINIMUM_APPROVED_CREDIT_SCORE) >= 0;
        boolean decision = loanRangeApproved && creditApproved;
        log.debug("Loan decision for userId={}: approved={} (rangeApproved={}, creditApproved={}).",
                userId, decision, loanRangeApproved, creditApproved);

        BigDecimal maxLoanAmount = findMaximumLoan(loanPeriod, userCreditModifier);
        log.debug("Maximum loan amount for userId={} at period={} months: {}.", userId, loanPeriod, maxLoanAmount);

        Integer minimumAllowedPeriod = null;
        if (maxLoanAmount.compareTo(MAXIMUM_ALLOWED_LOAN_AMOUNT) >= 0) { // Cap loan amount
            log.debug("Max loan amount {} exceeds cap for userId={}, capping at {}.",
                    maxLoanAmount, userId, MAXIMUM_ALLOWED_LOAN_AMOUNT);
            maxLoanAmount = MAXIMUM_ALLOWED_LOAN_AMOUNT;
        } else if (maxLoanAmount.compareTo(MINIMUM_ALLOWED_LOAN_AMOUNT) < 0) {
            log.info("Max loan amount {} below minimum for userId={}, searching for closest allowed period.",
                    maxLoanAmount, userId);
            minimumAllowedPeriod = findClosestAllowedLoan(userCreditModifier);
            log.debug("Closest allowed period for userId={}: {} months.", userId, minimumAllowedPeriod);
            if (minimumAllowedPeriod > MAXIMUM_ALLOWED_LOAN_PERIOD) {
                log.info("No valid loan period found for userId={}: minimum period {} exceeds maximum allowed {}.",
                        userId, minimumAllowedPeriod, MAXIMUM_ALLOWED_LOAN_PERIOD);
                throw new NoAllowedLoanException();
            }
            maxLoanAmount = findMaximumLoan(minimumAllowedPeriod, userCreditModifier);
            log.debug("Recalculated max loan amount for userId={} at adjusted period={} months: {}.",
                    userId, minimumAllowedPeriod, maxLoanAmount);
        }


        log.info("Loan request completed for userId={}: approved={}, maxAmount={}, updatedPeriod={}.",
                userId, decision, maxLoanAmount, minimumAllowedPeriod);

        return LoanResponse.builder()
                .amount(maxLoanAmount)
                .approved(decision)
                .updatedMinimumLoanPeriod(minimumAllowedPeriod)
                .build();
    }

    /**
     * Simplified scoring algorithm: credit score = (credit modifier / loan_amount) * loan period.
     * @return credit score
     */
    private BigDecimal scoringAlgorithm(int creditModifier, int loanPeriod, BigDecimal loanAmount) {
        BigDecimal modifierDecimal = BigDecimal.valueOf(creditModifier);
        BigDecimal periodDecimal = BigDecimal.valueOf(loanPeriod);
        return (modifierDecimal.divide(loanAmount, 5, RoundingMode.HALF_UP)).multiply(periodDecimal);
    }

    /**
     * Find minimum loan user can take.
     * @param creditModifier credit modifier of user.
     * @return minimum period when a loan is allowed.
     */
    private int findClosestAllowedLoan(int creditModifier) {
        BigDecimal modifierDecimal = BigDecimal.valueOf(creditModifier);
        int nearestPeriod = MINIMUM_ALLOWED_LOAN_AMOUNT.divide(modifierDecimal, 5, RoundingMode.HALF_UP)
                .setScale(0, RoundingMode.CEILING)
                .intValue();
        if (nearestPeriod > MAXIMUM_ALLOWED_LOAN_PERIOD) throw new NoAllowedLoanException(); // Never thrown in scope of this task

        return nearestPeriod;
    }

    /**
     * Find maximum loan sum for a person.
     * Isolated amount from scoring algorithm, substituting credit score for 1 (maximum allowed loan)
     * @return maximum amount of loan by given parameters.
     */
    private BigDecimal findMaximumLoan(int loanPeriod, int creditModifier) {
        return BigDecimal.valueOf((long) loanPeriod * creditModifier);
    }

}

package proov.inbank.loan_engine;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import proov.inbank.loan_engine.entity.CreditSegment;
import proov.inbank.loan_engine.entity.MockLoan;
import proov.inbank.loan_engine.repository.MockLoanRepository;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataSeeder implements ApplicationRunner {
    private final MockLoanRepository mockLoanRepository;

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
    }

    private void seedUsers() {
        if (mockLoanRepository.count() > 0) {
            return;
        }

        List<MockLoan> loans = Arrays.asList(
                newLoan(49002010965L, CreditSegment.DEBT),
                newLoan(49002010976L, CreditSegment.SEGMENT_1),
                newLoan(49002010987L, CreditSegment.SEGMENT_2),
                newLoan(49002010998L, CreditSegment.SEGMENT_3)
        );
        mockLoanRepository.saveAll(loans);
    }

    private MockLoan newLoan(Long id, CreditSegment segment) {
        MockLoan loan = new MockLoan();
        loan.setId(id);
        loan.setSegment(segment);
        return loan;
    }
}
